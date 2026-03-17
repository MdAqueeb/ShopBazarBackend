package com.shopbazar.shopbazar.dto;

import lombok.Data;

@Data
public class InventoryStockRequest {
    private Integer quantity; 
    private Integer stock;
}
