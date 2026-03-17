package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    @Query("SELECT i FROM Inventory i WHERE i.product.productId = :productId")
    Optional<Inventory> findByProduct_ProductId(@Param("productId") Long productId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Inventory i WHERE i.product.productId = :productId")
    boolean existsByProduct_ProductId(@Param("productId") Long productId);
    
    @Query("SELECT i FROM Inventory i JOIN i.product p JOIN p.seller s WHERE s.sellerId = :sellerId")
    Page<Inventory> findByProduct_Seller_SellerId(@Param("sellerId") Long sellerId, Pageable pageable);
    
    @Query("SELECT i FROM Inventory i WHERE i.stockQuantity <= :threshold")
    Page<Inventory> findByStockQuantityLessThanEqual(@Param("threshold") Integer threshold, Pageable pageable);
    
    @Query("SELECT i FROM Inventory i WHERE i.stockQuantity = :stockQuantity")
    Page<Inventory> findByStockQuantity(@Param("stockQuantity") Integer stockQuantity, Pageable pageable);
}
