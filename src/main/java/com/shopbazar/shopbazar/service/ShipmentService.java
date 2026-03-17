package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.ShipmentCreateRequest;
import com.shopbazar.shopbazar.dto.ShipmentResponse;
import com.shopbazar.shopbazar.entity.Order;
import com.shopbazar.shopbazar.entity.Shipment;
import com.shopbazar.shopbazar.entity.ShipmentTracking;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.OrderRepository;
import com.shopbazar.shopbazar.repository.ShipmentRepository;
import com.shopbazar.shopbazar.repository.ShipmentTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final ShipmentTrackingRepository shipmentTrackingRepository;

    @Transactional
    public ShipmentResponse createShipment(ShipmentCreateRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        // Check if shipment already exists for this order
        shipmentRepository.findByOrder_OrderId(request.getOrderId())
                .ifPresent(s -> {
                    throw new ConflictException("Shipment already exists for order id: " + request.getOrderId());
                });

        Shipment shipment = Shipment.builder()
                .order(order)
                .address(order.getAddress())
                .shipmentStatus("SHIPPED")
                .trackingNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase()) // Mock tracking number
                .estimatedDelivery(request.getEstimatedDelivery())
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);

        // Initial tracking entry
        ShipmentTracking tracking = ShipmentTracking.builder()
                .shipment(savedShipment)
                .status("SHIPPED")
                .location("Warehouse")
                .description("Shipment picked up by " + request.getCarrier())
                .build();
        shipmentTrackingRepository.save(tracking);

        return mapToResponse(savedShipment);
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentById(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));
        return mapToResponse(shipment);
    }

    @Transactional
    public ShipmentResponse updateShipmentStatus(Long shipmentId, String status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        shipment.setShipmentStatus(status);
        Shipment savedShipment = shipmentRepository.save(shipment);

        // Add tracking entry
        ShipmentTracking tracking = ShipmentTracking.builder()
                .shipment(savedShipment)
                .status(status)
                .location("In Transit")
                .description("Shipment status updated to " + status)
                .build();
        shipmentTrackingRepository.save(tracking);

        return mapToResponse(savedShipment);
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByOrderId(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found for order id: " + orderId));
        return mapToResponse(shipment);
    }

    private ShipmentResponse mapToResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .shipmentId(shipment.getShipmentId())
                .orderId(shipment.getOrder().getOrderId())
                .shipmentStatus(shipment.getShipmentStatus())
                .trackingNumber(shipment.getTrackingNumber())
                .estimatedDelivery(shipment.getEstimatedDelivery())
                .build();
    }
}
