package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.SellerAnalyticsResponse;
import com.shopbazar.shopbazar.dto.SellerApplyRequest;
import com.shopbazar.shopbazar.dto.SellerDashboardResponse;
import com.shopbazar.shopbazar.dto.SellerUpdateRequest;
import com.shopbazar.shopbazar.entity.OrderItem;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.OrderItemRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import com.shopbazar.shopbazar.repository.SellerRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
    }

    @Transactional
    public Seller updateSellerProfile(Long sellerId, SellerUpdateRequest request) {
        Seller seller = getSellerById(sellerId);
        if (request.getStoreName() != null) seller.setStoreName(request.getStoreName());
        if (request.getDescription() != null) seller.setDescription(request.getDescription());
        return sellerRepository.save(seller);
    }

    @Transactional(readOnly = true)
    public Page<Product> getSellerProducts(Long sellerId, Product.Status status, Pageable pageable) {
        getSellerById(sellerId); // Verify existence
        if (status != null) {
            return productRepository.findBySeller_SellerIdAndStatus(sellerId, status, pageable);
        }
        return productRepository.findBySeller_SellerId(sellerId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OrderItem> getSellerOrders(Long sellerId, Pageable pageable) {
        getSellerById(sellerId); // Verify existence
        return orderItemRepository.findBySeller_SellerId(sellerId, pageable);
    }

    @Transactional(readOnly = true)
    public SellerAnalyticsResponse getSellerAnalytics(Long sellerId, LocalDateTime startDate, LocalDateTime endDate) {
        getSellerById(sellerId); // Verify existence
        if (startDate == null || endDate == null) {
            throw new BadRequestException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }
        
        BigDecimal revenue = orderItemRepository.sumRevenueBySellerAndDateRange(sellerId, startDate, endDate);
        Long orderCount = orderItemRepository.countOrdersBySellerAndDateRange(sellerId, startDate, endDate);

        return SellerAnalyticsResponse.builder()
                .sellerId(sellerId)
                .totalRevenue(revenue)
                .totalOrders(orderCount)
                .build();
    }

    @Transactional
    public Seller applyForSeller(SellerApplyRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (sellerRepository.existsByUser_UserId(user.getUserId())) {
            throw new BadRequestException("User has already applied for a seller profile");
        }

        Seller seller = Seller.builder()
                .user(user)
                .storeName(request.getStoreName())
                .description(request.getStoreDescription())
                .email(user.getEmail()) // Typically use user's email or specific 
                .phone(request.getPhone())
                .gstNumber(request.getGstNumber())
                .status(Seller.Status.PENDING) // Default state
                .build();

        return sellerRepository.save(seller);
    }

    @Transactional(readOnly = true)
    public SellerDashboardResponse getSellerDashboard(Long sellerId) {
        Seller seller = getSellerById(sellerId);
        Long totalProducts = productRepository.countBySeller_SellerId(sellerId);
        Long totalOrders = orderItemRepository.countBySeller_SellerId(sellerId);
        BigDecimal totalRevenue = orderItemRepository.sumTotalRevenueBySeller(sellerId);

        return SellerDashboardResponse.builder()
                .sellerId(sellerId)
                .storeName(seller.getStoreName())
                .status(seller.getStatus().name())
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .build();
    }

    @Transactional
    public Seller deactivateSeller(Long sellerId) {
        Seller seller = getSellerById(sellerId);
        seller.setStatus(Seller.Status.REJECTED); // Or create INACTIVE status enum
        return sellerRepository.save(seller);
    }
}
