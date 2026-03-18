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
@Schema(description = "Response containing current tracking information")
public class TrackingResponse {
    @Schema(description = "Tracking entry ID", example = "1")
    private Long trackingId;
    @Schema(description = "Shipment ID", example = "1")
    private Long shipmentId;
    @Schema(description = "Current status", example = "OUT_FOR_DELIVERY")
    private String status;
    @Schema(description = "Tracking message or update", example = "Package arrived at local facility")
    private String message;
}
