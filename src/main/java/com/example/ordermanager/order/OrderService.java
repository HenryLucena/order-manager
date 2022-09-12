package com.example.ordermanager.order;

import com.example.ordermanager.configuration.email.EmailSenderService;
import com.example.ordermanager.configuration.handler.InternalServerErrorException;
import com.example.ordermanager.configuration.handler.NotFoundException;
import com.example.ordermanager.item.ItemService;
import com.example.ordermanager.stock.StockMovement;
import com.example.ordermanager.stock.StockMovementService;
import com.example.ordermanager.users.UserService;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
public class OrderService {

    private static final Logger logCommon = LogManager.getLogger("managerLogger");
    private static final Logger errorLog = LogManager.getLogger("errorLogger");

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    StockMovementService stockMovementService;

    @Autowired
    EmailSenderService senderService;

    public String create(CreateOrderRequest request) {
        Order order = this.createOrder(request);

        String result = this.completeOrder(order);

        return result;
    }


    public String completeOrder(Order order){
        StockMovement stockMovement = stockMovementService.getAvaliableStock(order.getItem().getId());

        if (isNull(stockMovement)) return format("Order created, awaiting for stock. status: %s", order.getOrderStatus());

        int tempCurrentQuantity = order.getCurrentQuantity() - stockMovement.getCurrentQuantity();

        if (tempCurrentQuantity <= 0){
            stockMovement.setCurrentQuantity( stockMovement.getCurrentQuantity() - order.getCurrentQuantity() );

            order.setCurrentQuantity(0);
            order.setOrderStatus(OrderStatus.COMPLETED);

            this.createHistoryAndSave(order, stockMovement);

            senderService.sendEmail(order.getUser().getEmail(), format("Your order %s has been completed", order.getId()));
            logCommon.info(format("Order %s has been completed", order.getId()));
            return format("Order created and finished, status: %s", order.getOrderStatus());
        } else {
            stockMovement.setCurrentQuantity(0);
            order.setCurrentQuantity(tempCurrentQuantity);

            this.createHistoryAndSave(order, stockMovement);
        }

        if (order.getCurrentQuantity() > 0) {
            this.completeOrder(order);
        }

        return format("Order created and finished, status: %s", order.getOrderStatus());
    }

    private Order createOrder(CreateOrderRequest request){
        Order order = new Order();
        order.setItem(itemService.getItemById(request.getItemId()));
        order.setQuantity(request.getQuantity());
        order.setUser(userService.getUser(request.getUserId()));
        order.setCurrentQuantity(request.getQuantity());

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public Order update(long orderId, CreateOrderRequest request) {
        try{
            Order order = orderRepository.findById(orderId).get();

            if (isNull(order)) throw new NotFoundException("Order not found.");

            order.setCurrentQuantity(request.getQuantity());
            order.setItem(itemService.getItemById(request.getItemId()));
            order.setUser(userService.getUser(request.getUserId()));
            order.setQuantity(request.getQuantity());

            orderRepository.save(order);

            return order;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public void delete(long orderId) {
        try {
            Order order = orderRepository.findById(orderId).get();
            if (isNull(order)) throw new NotFoundException("Order not found.");

            orderRepository.delete(order);
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public List<Order> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            if (orders.isEmpty()) throw new NotFoundException("No order found.");

            return orders;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public Order getOrder(long orderIr) {
        try {
            Order order = orderRepository.findById(orderIr).get();

            if (isNull(order)) throw new NotFoundException("Order not found.");

            return order;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
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

        stockMovementService.updateStock(stockMovement);
        orderRepository.save(order);
    }

}
