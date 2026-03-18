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
@Schema(description = "Response containing payment transaction details")
public class PaymentResponse {
    @Schema(description = "Payment ID", example = "1")
    private Long paymentId;
    @Schema(description = "Order ID", example = "1")
    private Long orderId;
    @Schema(description = "Transaction amount", example = "1050.50")
    private BigDecimal amount;
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;
    @Schema(description = "Current payment status", example = "COMPLETED")
    private String paymentStatus;
    @Schema(description = "Gateway specific transaction ID", example = "txn_123456789")
    private String gatewayTransaction;
}
