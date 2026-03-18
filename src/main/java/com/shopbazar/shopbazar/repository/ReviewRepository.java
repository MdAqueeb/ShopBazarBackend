package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProduct_ProductId(Long productId, Pageable pageable);
    java.util.List<Review> findByProduct_ProductId(Long productId);
    boolean existsByProduct_ProductIdAndUser_UserId(Long productId, Long userId);
}
