package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing details of an item within an order")
public class OrderItemResponse {
    @Schema(description = "Order item ID", example = "1")
    private Long orderItemId;
    @Schema(description = "Product ID", example = "1")
    private Long productId;
    @Schema(description = "Name of the product", example = "iPhone 15 Pro")
    private String productName;
    @Schema(description = "Quantity ordered", example = "1")
    private Integer quantity;
    @Schema(description = "Price per unit at the time of order", example = "999.99")
    private BigDecimal price;
}
