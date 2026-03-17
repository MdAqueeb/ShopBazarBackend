package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Shipment;
import com.shopbazar.shopbazar.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Optional<Shipment> getShipmentById(Long shipmentId) {
        return shipmentRepository.findById(shipmentId);
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment updateShipment(Long shipmentId, Shipment shipment) {
        Shipment existing = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));
        existing.setShipmentStatus(shipment.getShipmentStatus());
        existing.setTrackingNumber(shipment.getTrackingNumber());
        existing.setEstimatedDelivery(shipment.getEstimatedDelivery());
        existing.setAddress(shipment.getAddress());
        return shipmentRepository.save(existing);
    }

    public void deleteShipment(Long shipmentId) {
        shipmentRepository.deleteById(shipmentId);
    }
}
