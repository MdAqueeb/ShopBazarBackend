package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.service.ShipmentService;
import com.shopbazar.shopbazar.service.ShipmentTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipmentTrackingService shipmentTrackingService;

    @PostMapping("/shipments")
    public ResponseEntity<ShipmentResponse> createShipment(@RequestBody ShipmentCreateRequest request) {
        return new ResponseEntity<>(shipmentService.createShipment(request), HttpStatus.CREATED);
    }

    @GetMapping("/shipments/{shipmentId}")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
    }

    @PutMapping("/shipments/{shipmentId}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @RequestBody ShipmentStatusUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId, request.getStatus()));
    }

    @GetMapping("/orders/{orderId}/shipment")
    public ResponseEntity<ShipmentResponse> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    @PostMapping("/shipments/{shipmentId}/tracking")
    public ResponseEntity<TrackingResponse> addTrackingUpdate(
            @PathVariable Long shipmentId,
            @RequestBody TrackingUpdateRequest request) {
        return new ResponseEntity<>(shipmentTrackingService.addTrackingUpdate(shipmentId, request), HttpStatus.CREATED);
    }

    @GetMapping("/shipments/{shipmentId}/tracking")
    public ResponseEntity<TrackingHistoryResponse> getTrackingHistory(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentTrackingService.getTrackingHistory(shipmentId));
    }
}
