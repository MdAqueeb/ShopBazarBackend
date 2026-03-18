package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing order details")
public class OrderResponse {
    @Schema(description = "Order ID", example = "1")
    private Long orderId;
    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "Shipping address ID", example = "1")
    private Long addressId;
    @Schema(description = "Total order amount", example = "1050.50")
    private BigDecimal totalAmount;
    @Schema(description = "Current order status", example = "PENDING")
    private String orderStatus;
    @Schema(description = "Current payment status", example = "UNPAID")
    private String paymentStatus;
    @Schema(description = "Order creation timestamp", example = "2024-03-20T10:15:30")
    private LocalDateTime createdAt;
    @Schema(description = "List of items in the order")
    private List<OrderItemResponse> orderItems;
}
