package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.SellerAnalyticsResponse;
import com.shopbazar.shopbazar.dto.SellerApplyRequest;
import com.shopbazar.shopbazar.dto.SellerDashboardResponse;
import com.shopbazar.shopbazar.dto.SellerUpdateRequest;
import com.shopbazar.shopbazar.entity.OrderItem;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Seller", description = "Endpoints for seller profile management, store analytics, and store operations")
public class SellerController {

    private final SellerService sellerService;

    // 1. Get seller profile
    @Operation(summary = "Get seller profile", description = "Retrieves public or private profile details for a seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @GetMapping("/{sellerId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Seller>> getSellerProfile(
            @Parameter(description = "ID of the seller", required = true)
            @PathVariable Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller profile retrieved successfully")
                .data(seller)
                .build());
    }

    // 2. Update seller profile
    @Operation(summary = "Update seller profile", description = "Updates store information and contact details for a seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @PutMapping("/{sellerId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Seller>> updateSellerProfile(
            @Parameter(description = "ID of the seller to update", required = true)
            @PathVariable Long sellerId, @RequestBody SellerUpdateRequest request) {
        Seller updatedSeller = sellerService.updateSellerProfile(sellerId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller profile updated successfully")
                .data(updatedSeller)
                .build());
    }

    // 3. Get seller products
    @Operation(summary = "Get seller products", description = "Lists all products belonging to a specific seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/{sellerId}/products")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Product>>> getSellerProducts(
            @Parameter(description = "ID of the seller", required = true)
            @PathVariable Long sellerId,
            @Parameter(description = "Filter by product status")
            @RequestParam(required = false) Product.Status status,
            Pageable pageable) {
        Page<Product> products = sellerService.getSellerProducts(sellerId, status, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .data(products)
                .build());
    }

    // 4. Get seller orders
    @Operation(summary = "Get seller orders", description = "Lists all order items that include products from this seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/{sellerId}/orders")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<OrderItem>>> getSellerOrders(
            @Parameter(description = "ID of the seller", required = true)
            @PathVariable Long sellerId, Pageable pageable) {
        Page<OrderItem> orders = sellerService.getSellerOrders(sellerId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<OrderItem>>builder()
                .success(true)
                .message("Orders retrieved successfully")
                .data(orders)
                .build());
    }

    // 5. Get seller analytics
    @Operation(summary = "Get seller analytics", description = "Retrieves store performance metrics for a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully")
    })
    @GetMapping("/{sellerId}/analytics")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<SellerAnalyticsResponse>> getSellerAnalytics(
            @Parameter(description = "ID of the seller", required = true)
            @PathVariable Long sellerId,
            @Parameter(description = "Start date for analytics", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for analytics", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        SellerAnalyticsResponse analytics = sellerService.getSellerAnalytics(sellerId, startDate, endDate);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<SellerAnalyticsResponse>builder()
                .success(true)
                .message("Analytics retrieved successfully")
                .data(analytics)
                .build());
    }

    // 6. Apply for seller account
    @Operation(summary = "Apply for seller account", description = "Submits a request to become a seller on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid application data")
    })
    @PostMapping("/apply")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Seller>> applyForSeller(@RequestBody SellerApplyRequest request) {
        Seller createdSeller = sellerService.applyForSeller(request);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller application submitted successfully")
                .data(createdSeller)
                .build(), HttpStatus.CREATED);
    }

    // 7. Get seller dashboard
    @Operation(summary = "Get seller dashboard", description = "Retrieves high-level summary metrics for the seller dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully")
    })
    @GetMapping("/{sellerId}/dashboard")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<SellerDashboardResponse>> getSellerDashboard(
            @Parameter(description = "ID of the seller", required = true)
            @PathVariable Long sellerId) {
        SellerDashboardResponse dashboard = sellerService.getSellerDashboard(sellerId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<SellerDashboardResponse>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(dashboard)
                .build());
    }

    // 8. Deactivate seller
    @Operation(summary = "Deactivate seller", description = "Deactivates a seller's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @PutMapping("/{sellerId}/deactivate")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Seller>> deactivateSeller(
            @Parameter(description = "ID of the seller to deactivate", required = true)
            @PathVariable Long sellerId) {
        Seller deactivatedSeller = sellerService.deactivateSeller(sellerId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Seller>builder()
                .success(true)
                .message("Seller deactivated successfully")
                .data(deactivatedSeller)
                .build());
    }
}
