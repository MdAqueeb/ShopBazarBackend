package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByUser_UserId(Long userId);
    Optional<Seller> findByUser_UserId(Long userId);
    Page<Seller> findByStatus(Seller.Status status, Pageable pageable);
}
