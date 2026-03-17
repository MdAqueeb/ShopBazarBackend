package com.shopbazar.shopbazar.dto;

import lombok.Data;

@Data
public class InventoryCreateRequest {
    private Long productId;
    private Integer stock;
}
