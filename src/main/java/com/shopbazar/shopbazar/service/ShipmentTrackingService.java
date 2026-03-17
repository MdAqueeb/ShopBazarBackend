package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.TrackingHistoryResponse;
import com.shopbazar.shopbazar.dto.TrackingResponse;
import com.shopbazar.shopbazar.dto.TrackingUpdateRequest;
import com.shopbazar.shopbazar.entity.Shipment;
import com.shopbazar.shopbazar.entity.ShipmentTracking;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.ShipmentRepository;
import com.shopbazar.shopbazar.repository.ShipmentTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentTrackingService {

    private final ShipmentTrackingRepository shipmentTrackingRepository;
    private final ShipmentRepository shipmentRepository;

    @Transactional
    public TrackingResponse addTrackingUpdate(Long shipmentId, TrackingUpdateRequest request) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        ShipmentTracking tracking = ShipmentTracking.builder()
                .shipment(shipment)
                .status(request.getStatus())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();

        ShipmentTracking savedTracking = shipmentTrackingRepository.save(tracking);

        // Update shipment status as well
        shipment.setShipmentStatus(request.getStatus());
        shipmentRepository.save(shipment);

        return TrackingResponse.builder()
                .trackingId(savedTracking.getTrackingId())
                .shipmentId(shipmentId)
                .status(savedTracking.getStatus())
                .message("Tracking update added")
                .build();
    }

    @Transactional(readOnly = true)
    public TrackingHistoryResponse getTrackingHistory(Long shipmentId) {
        if (!shipmentRepository.existsById(shipmentId)) {
            throw new ResourceNotFoundException("Shipment not found with id: " + shipmentId);
        }

        List<ShipmentTracking> trackingList = shipmentTrackingRepository.findByShipment_ShipmentIdOrderByUpdatedAtAsc(shipmentId);

        return TrackingHistoryResponse.builder()
                .shipmentId(shipmentId)
                .tracking(trackingList.stream()
                        .map(t -> TrackingHistoryResponse.TrackingItem.builder()
                                .status(t.getStatus())
                                .location(t.getLocation())
                                .description(t.getDescription())
                                .time(t.getUpdatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
