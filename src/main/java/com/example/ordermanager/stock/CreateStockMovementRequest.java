package com.example.ordermanager.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStockMovementRequest {
    private int quantity;
    private long itemId;
}
