package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update the status of an order")
public class OrderStatusUpdateRequest {
    @Schema(description = "New order status", example = "SHIPPED")
    private String status;
}
