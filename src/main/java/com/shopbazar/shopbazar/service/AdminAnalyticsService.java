package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.PlatformAnalyticsResponse;
import com.shopbazar.shopbazar.repository.OrderRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import com.shopbazar.shopbazar.repository.SellerRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PlatformAnalyticsResponse getPlatformAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        Long totalUsers = userRepository.count();
        Long totalSellers = sellerRepository.count();
        Long totalProducts = productRepository.count();
        Long totalOrders = orderRepository.count();
        
        // Mock revenue calculation - in a real app, this would be a sum query on orders within the date range
        BigDecimal totalRevenue = BigDecimal.valueOf(1250000.00); 

        return PlatformAnalyticsResponse.builder()
                .totalUsers(totalUsers)
                .totalSellers(totalSellers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .build();
    }
}
