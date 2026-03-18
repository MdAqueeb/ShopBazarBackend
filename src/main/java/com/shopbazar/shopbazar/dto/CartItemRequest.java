package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to add or update an item in the cart")
public class CartItemRequest {
    @Schema(description = "Product ID", example = "1")
    private Long productId;
    @Schema(description = "Quantity of the product", example = "2")
    private Integer quantity;
}
