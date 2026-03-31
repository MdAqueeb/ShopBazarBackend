package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.dto.ProductResponse;
import com.shopbazar.shopbazar.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByStatus(Product.Status status, Pageable pageable);
    Page<Product> findBySeller_SellerId(Long sellerId, Pageable pageable);
    
    Page<Product> findBySeller_SellerIdAndStatus(Long sellerId, Product.Status status, Pageable pageable);
    
    Long countBySeller_SellerId(Long sellerId);

    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);
    
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descKeyword, Pageable pageable);

    @Query("SELECT p.name FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findSuggestions(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.ratings r " +
           "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "GROUP BY p " +
           "HAVING (:minRating IS NULL OR AVG(r.rating) >= :minRating)")
    Page<Product> filterProducts(@Param("keyword") String keyword, 
                                @Param("categoryId") Long categoryId, 
                                @Param("minPrice") BigDecimal minPrice, 
                                @Param("maxPrice") BigDecimal maxPrice, 
                                @Param("minRating") Double minRating, 
                                Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.orderItems oi WHERE oi.order.createdAt > :since GROUP BY p ORDER BY SUM(oi.quantity) DESC")
    Page<Product> findTrendingProducts(@Param("since") LocalDateTime since, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.orderItems oi GROUP BY p ORDER BY SUM(oi.quantity) DESC")
    Page<Product> findPopularProducts(Pageable pageable);
}
