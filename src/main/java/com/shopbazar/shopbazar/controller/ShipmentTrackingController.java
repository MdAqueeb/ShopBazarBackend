package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.ShipmentTrackingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipment-tracking")
@RequiredArgsConstructor
@Tag(name = "Shipment Tracking", description = "Endpoints for managing real-time shipment tracking data (Placeholder)")
public class ShipmentTrackingController {

    private final ShipmentTrackingService shipmentTrackingService;
}
