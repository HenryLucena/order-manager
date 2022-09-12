package com.example.ordermanager.stock;

import com.example.ordermanager.configuration.email.EmailSenderService;
import com.example.ordermanager.configuration.handler.InternalServerErrorException;
import com.example.ordermanager.configuration.handler.NotFoundException;
import com.example.ordermanager.item.ItemService;
import com.example.ordermanager.order.Order;
import com.example.ordermanager.order.OrderRepository;
import com.example.ordermanager.order.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
public class StockMovementService {

    private static final Logger logCommon = LogManager.getLogger("managerLogger");
    private static final Logger errorLog = LogManager.getLogger("errorLogger");

    @Autowired
    ItemService itemService;

    @Autowired
    StockMovementRepository stockMovementRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EmailSenderService senderService;

    public StockMovement getAvaliableStock(long itemId) {
        return stockMovementRepository.getAvaliableStock(itemId);
    }

    @Transactional
    public void updateStock(StockMovement stockMovement) {
        try {
            stockMovementRepository.save(stockMovement);
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @Transactional
    public String create(CreateStockMovementRequest request) {
        StockMovement stockMovement = this.createStockMovement(request);
        stockMovementRepository.save(stockMovement);
        logCommon.info(format("Stock movement has been created, id: %s", stockMovement.getId()));

        Order order = this.verifyOrderPending();
        if (isNull(order)) return "Stock movement registered, no orders pending";

        return this.completeOrder(order, stockMovement);
    }

    private String completeOrder(Order order, StockMovement stockMovement){

        int tempCurrentQuantityOrder = order.getCurrentQuantity() - stockMovement.getCurrentQuantity();

        if (tempCurrentQuantityOrder <= 0) {
            stockMovement.setCurrentQuantity(stockMovement.getCurrentQuantity() - order.getCurrentQuantity());
            order.setCurrentQuantity(0);
            order.setOrderStatus(OrderStatus.COMPLETED);

            this.createHistoryAndSave(order, stockMovement);

            senderService.sendEmail(order.getUser().getEmail(), format("Your order %s has been completed", order.getId()));

            logCommon.info(format("Order %s has been completed", order.getId()));
        } else {
            stockMovement.setCurrentQuantity(0);
            order.setCurrentQuantity(tempCurrentQuantityOrder);

            this.createHistoryAndSave(order, stockMovement);

            logCommon.info(format("Stock movement id: %s has been completed. There are still pending orders"), stockMovement.getId());
            return "Stock movement registered, there are orders pending";
        }

        if (stockMovement.getCurrentQuantity() > 0){
            Order newOrder = this.verifyOrderPending();
            if (isNull(newOrder)) {
                return  "Stock movement registered, no orders pending";
            }

            this.completeOrder(newOrder, stockMovement);
        }

        if (isNull(this.verifyOrderPending())) {
            return "Stock movement registered, there are no orders pending";
        }

        logCommon.info(format("Stock movement id: %s has been completed. There are no orders pending"), stockMovement.getId());
        return "Stock movement registered, there are orders pending";
    }

    public StockMovement getStockById(long stockMovementId){
        try {
            StockMovement stockMovement = stockMovementRepository.findById(stockMovementId).get();

            if (isNull(stockMovement)) throw new NotFoundException("Stock movement not found");

            return stockMovement;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    private StockMovement createStockMovement(CreateStockMovementRequest request) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setCurrentQuantity(request.getQuantity());
        stockMovement.setItem(itemService.getItemById(request.getItemId()));
        stockMovement.setQuantity(request.getQuantity());

        return stockMovement;
    }

    private Order verifyOrderPending() {
        Order order = orderRepository.getOrderPending();

        return order;
    }

    @Transactional
    private void createHistoryAndSave(Order order, StockMovement stockMovement){
        List<?> orderHistory = order.getStockMovementHistory();

        if (isNull(orderHistory)) {
            List<StockMovement> newOrderHistory = new ArrayList<>();
            newOrderHistory.add(stockMovement);
            order.setStockMovementHistory(newOrderHistory);
        } else {
            order.getStockMovementHistory().add(stockMovement);
        }

        List<?> stockHistory = stockMovement.getOrderHistory();

        if(isNull(stockHistory)) {
            List<Order> newStockHistory = new ArrayList<>();
            newStockHistory.add(order);
            stockMovement.setOrderHistory(newStockHistory);
        } else {
            stockMovement.getOrderHistory().add(order);
        }

        stockMovementRepository.save(stockMovement);
        orderRepository.save(order);
    }

    public StockMovement update(CreateStockMovementRequest request, long stockId) {
        try {
            StockMovement stockMovement = stockMovementRepository.findById(stockId).get();

            if (isNull(stockMovement)) throw new NotFoundException("Stock not found");

            stockMovement.setItem(itemService.getItemById(request.getItemId()));
            stockMovement.setQuantity(request.getQuantity());
            stockMovement.setCurrentQuantity(request.getQuantity());
            stockMovementRepository.save(stockMovement);

            return stockMovement;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }


    public void delete(long stockId) {
        try {
            StockMovement stockMovement = stockMovementRepository.findById(stockId).get();

            if (isNull(stockMovement)) throw new NotFoundException("Stock not found");

            stockMovementRepository.delete(stockMovement);
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public List<StockMovement> getAllStocks() {
        try {
            List<StockMovement> stockMovementList = stockMovementRepository.findAll();

            if (stockMovementList.isEmpty()) throw new NotFoundException("Stock not found");

            return stockMovementList;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }
}
