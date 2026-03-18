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
@Schema(description = "Response containing shipment details")
public class ShipmentResponse {
    @Schema(description = "Shipment ID", example = "1")
    private Long shipmentId;
    @Schema(description = "Order ID", example = "1")
    private Long orderId;
    @Schema(description = "Current shipment status", example = "IN_TRANSIT")
    private String shipmentStatus;
    @Schema(description = "Tracking number", example = "TRACK123456789")
    private String trackingNumber;
    @Schema(description = "Estimated delivery date", example = "2024-03-25")
    private LocalDate estimatedDelivery;
}
