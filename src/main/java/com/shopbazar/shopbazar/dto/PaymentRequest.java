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
@Schema(description = "Request to initiate a payment")
public class PaymentRequest {
    @Schema(description = "Order ID to pay for", example = "1")
    private Long orderId;
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;
    @Schema(description = "Payment amount", example = "1050.50")
    private BigDecimal amount;
}
