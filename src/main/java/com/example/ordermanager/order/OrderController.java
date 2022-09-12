package com.example.ordermanager.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest request){
        String result = orderService.create(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> update(@PathVariable long orderId,
                                    @RequestBody CreateOrderRequest request){
        Order order = orderService.update(orderId, request);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> delete(@PathVariable long orderId){
        orderService.delete(orderId);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable long orderIr){
        Order order = orderService.getOrder(orderIr);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}

