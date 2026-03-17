package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.ShipmentTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentTrackingRepository extends JpaRepository<ShipmentTracking, Long> {
    List<ShipmentTracking> findByShipment_ShipmentIdOrderByUpdatedAtAsc(Long shipmentId);
}
