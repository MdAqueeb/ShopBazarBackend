package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to update inventory stock level")
public class InventoryStockRequest {
    @Schema(description = "Quantity to add/remove (not used in current logic, placeholder)", example = "10")
    private Integer quantity; 
    @Schema(description = "New absolute stock level", example = "110")
    private Integer stock;
}
