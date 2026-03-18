package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.service.ShipmentService;
import com.shopbazar.shopbazar.service.ShipmentTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Shipment", description = "Endpoints for managing logistics, shipment creation, and real-time tracking")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipmentTrackingService shipmentTrackingService;

    @Operation(summary = "Create shipment", description = "Initializes a shipment for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shipment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid shipment data")
    })
    @PostMapping("/shipments")
    public ResponseEntity<ShipmentResponse> createShipment(@RequestBody ShipmentCreateRequest request) {
        return new ResponseEntity<>(shipmentService.createShipment(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get shipment by ID", description = "Retrieves details of a specific shipment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @GetMapping("/shipments/{shipmentId}")
    public ResponseEntity<ShipmentResponse> getShipmentById(
            @Parameter(description = "ID of the shipment", required = true)
            @PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
    }

    @Operation(summary = "Update shipment status", description = "Updates the current status of a shipment (e.g., Shipped, Delivered)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @PutMapping("/shipments/{shipmentId}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
            @Parameter(description = "ID of the shipment", required = true)
            @PathVariable Long shipmentId,
            @RequestBody ShipmentStatusUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId, request.getStatus()));
    }

    @Operation(summary = "Get shipment by order ID", description = "Retrieves shipment details linked to a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order or shipment not found")
    })
    @GetMapping("/orders/{orderId}/shipment")
    public ResponseEntity<ShipmentResponse> getShipmentByOrderId(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    @Operation(summary = "Add tracking update", description = "Logs a new location or status update for a shipment's journey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tracking update added successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @PostMapping("/shipments/{shipmentId}/tracking")
    public ResponseEntity<TrackingResponse> addTrackingUpdate(
            @Parameter(description = "ID of the shipment", required = true)
            @PathVariable Long shipmentId,
            @RequestBody TrackingUpdateRequest request) {
        return new ResponseEntity<>(shipmentTrackingService.addTrackingUpdate(shipmentId, request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get tracking history", description = "Retrieves the full chronological history of tracking updates for a shipment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracking history retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @GetMapping("/shipments/{shipmentId}/tracking")
    public ResponseEntity<TrackingHistoryResponse> getTrackingHistory(
            @Parameter(description = "ID of the shipment", required = true)
            @PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentTrackingService.getTrackingHistory(shipmentId));
    }
}
