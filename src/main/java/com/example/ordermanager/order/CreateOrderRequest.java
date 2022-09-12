package com.example.ordermanager.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    private long itemId;
    private long userId;
    private int quantity;
}
