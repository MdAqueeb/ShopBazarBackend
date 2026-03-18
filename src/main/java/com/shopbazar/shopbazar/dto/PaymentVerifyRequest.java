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
@Schema(description = "Request to verify a payment transaction")
public class PaymentVerifyRequest {
    @Schema(description = "Payment ID to verify", example = "1")
    private Long paymentId;
    @Schema(description = "Transaction ID from the gateway", example = "txn_123456789")
    private String gatewayTransactionId;
}
