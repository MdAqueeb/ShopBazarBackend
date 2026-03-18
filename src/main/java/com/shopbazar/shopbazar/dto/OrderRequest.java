package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to place a new order")
public class OrderRequest {
    @Schema(description = "User ID placing the order", example = "1")
    private Long userId;
    @Schema(description = "Shipping address ID", example = "1")
    private Long addressId;
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;
}
