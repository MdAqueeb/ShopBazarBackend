package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create a shipment for an order")
public class ShipmentCreateRequest {
    @Schema(description = "Order ID to ship", example = "1")
    private Long orderId;
    @Schema(description = "Shipping carrier", example = "FedEx")
    private String carrier;
    @Schema(description = "Estimated delivery date", example = "2024-03-25")
    private LocalDate estimatedDelivery;
}
