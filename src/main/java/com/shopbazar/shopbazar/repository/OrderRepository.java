package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);
    Page<Order> findByUser_UserIdAndOrderStatus(Long userId, String orderStatus, Pageable pageable);
    Page<Order> findByOrderStatus(String orderStatus, Pageable pageable);
}
