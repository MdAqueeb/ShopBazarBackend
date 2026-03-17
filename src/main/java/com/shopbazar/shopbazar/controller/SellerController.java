package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.dto.SellerAnalyticsResponse;
import com.shopbazar.shopbazar.dto.SellerApplyRequest;
import com.shopbazar.shopbazar.dto.SellerDashboardResponse;
import com.shopbazar.shopbazar.dto.SellerUpdateRequest;
import com.shopbazar.shopbazar.entity.OrderItem;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    // 1. Get seller profile
    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<Seller>> getSellerProfile(@PathVariable Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller profile retrieved successfully")
                .data(seller)
                .build());
    }

    // 2. Update seller profile
    @PutMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<Seller>> updateSellerProfile(
            @PathVariable Long sellerId, @RequestBody SellerUpdateRequest request) {
        Seller updatedSeller = sellerService.updateSellerProfile(sellerId, request);
        return ResponseEntity.ok(ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller profile updated successfully")
                .data(updatedSeller)
                .build());
    }

    // 3. Get seller products
    @GetMapping("/{sellerId}/products")
    public ResponseEntity<ApiResponse<Page<Product>>> getSellerProducts(
            @PathVariable Long sellerId,
            @RequestParam(required = false) Product.Status status,
            Pageable pageable) {
        Page<Product> products = sellerService.getSellerProducts(sellerId, status, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .data(products)
                .build());
    }

    // 4. Get seller orders
    @GetMapping("/{sellerId}/orders")
    public ResponseEntity<ApiResponse<Page<OrderItem>>> getSellerOrders(
            @PathVariable Long sellerId, Pageable pageable) {
        Page<OrderItem> orders = sellerService.getSellerOrders(sellerId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<OrderItem>>builder()
                .success(true)
                .message("Orders retrieved successfully")
                .data(orders)
                .build());
    }

    // 5. Get seller analytics
    @GetMapping("/{sellerId}/analytics")
    public ResponseEntity<ApiResponse<SellerAnalyticsResponse>> getSellerAnalytics(
            @PathVariable Long sellerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        SellerAnalyticsResponse analytics = sellerService.getSellerAnalytics(sellerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.<SellerAnalyticsResponse>builder()
                .success(true)
                .message("Analytics retrieved successfully")
                .data(analytics)
                .build());
    }

    // 6. Apply for seller account
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Seller>> applyForSeller(@RequestBody SellerApplyRequest request) {
        Seller createdSeller = sellerService.applyForSeller(request);
        return new ResponseEntity<>(ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller application submitted successfully")
                .data(createdSeller)
                .build(), HttpStatus.CREATED);
    }

    // 7. Get seller dashboard
    @GetMapping("/{sellerId}/dashboard")
    public ResponseEntity<ApiResponse<SellerDashboardResponse>> getSellerDashboard(
            @PathVariable Long sellerId) {
        SellerDashboardResponse dashboard = sellerService.getSellerDashboard(sellerId);
        return ResponseEntity.ok(ApiResponse.<SellerDashboardResponse>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(dashboard)
                .build());
    }

    // 8. Deactivate seller
    @PutMapping("/{sellerId}/deactivate")
    public ResponseEntity<ApiResponse<Seller>> deactivateSeller(@PathVariable Long sellerId) {
        Seller deactivatedSeller = sellerService.deactivateSeller(sellerId);
        return ResponseEntity.ok(ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller deactivated successfully")
                .data(deactivatedSeller)
                .build());
    }
}
