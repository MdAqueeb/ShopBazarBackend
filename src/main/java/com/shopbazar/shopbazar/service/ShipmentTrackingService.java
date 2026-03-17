package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.ShipmentTracking;
import com.shopbazar.shopbazar.repository.ShipmentTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentTrackingService {

    private final ShipmentTrackingRepository shipmentTrackingRepository;

    public ShipmentTracking createShipmentTracking(ShipmentTracking tracking) {
        return shipmentTrackingRepository.save(tracking);
    }

    public Optional<ShipmentTracking> getShipmentTrackingById(Long trackingId) {
        return shipmentTrackingRepository.findById(trackingId);
    }

    public List<ShipmentTracking> getAllShipmentTrackings() {
        return shipmentTrackingRepository.findAll();
    }

    public ShipmentTracking updateShipmentTracking(Long trackingId, ShipmentTracking tracking) {
        ShipmentTracking existing = shipmentTrackingRepository.findById(trackingId)
                .orElseThrow(() -> new RuntimeException("ShipmentTracking not found with id: " + trackingId));
        existing.setStatus(tracking.getStatus());
        existing.setLocation(tracking.getLocation());
        existing.setDescription(tracking.getDescription());
        return shipmentTrackingRepository.save(existing);
    }

    public void deleteShipmentTracking(Long trackingId) {
        shipmentTrackingRepository.deleteById(trackingId);
    }
}
