package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    Page<OrderItem> findBySeller_SellerId(Long sellerId, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(o.price * o.quantity), 0) FROM OrderItem o WHERE o.seller.sellerId = :sellerId AND o.order.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueBySellerAndDateRange(@Param("sellerId") Long sellerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.seller.sellerId = :sellerId AND o.order.createdAt BETWEEN :startDate AND :endDate")
    Long countOrdersBySellerAndDateRange(@Param("sellerId") Long sellerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(o.price * o.quantity), 0) FROM OrderItem o WHERE o.seller.sellerId = :sellerId")
    BigDecimal sumTotalRevenueBySeller(@Param("sellerId") Long sellerId);

    Long countBySeller_SellerId(Long sellerId);

    java.util.List<OrderItem> findByOrder_OrderId(Long orderId);

    java.util.Optional<OrderItem> findByOrder_OrderIdAndOrderItemId(Long orderId, Long orderItemId);
}
