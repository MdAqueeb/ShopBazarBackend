package com.shopbazar.shopbazar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    private Long shipmentId;
    private Long orderId;
    private String shipmentStatus;
    private String trackingNumber;
    private LocalDate estimatedDelivery;
}
