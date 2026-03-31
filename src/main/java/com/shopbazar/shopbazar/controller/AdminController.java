package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Administrative endpoints for platform-wide user, seller, and product moderation")
public class AdminController {

    private final UserService userService;
    private final SellerService sellerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final AdminAnalyticsService analyticsService;

    // --- User Management ---

    @Operation(summary = "List all users", description = "Retrieves a paginated list of all users on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @Parameter(description = "Filter by user status")
            @RequestParam(required = false) User.Status status,
            Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(userService.getUsersByStatus(status, pageable));
        }
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Block user", description = "Suspends a user account and prevents login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User blocked successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/users/{userId}/block")
    public ResponseEntity<User> blockUser(
            @Parameter(description = "ID of the user to block", required = true)
            @PathVariable Long userId, @RequestBody BlockRequest request) {
        return ResponseEntity.ok(userService.blockUser(userId, request.getReason()));
    }

    @Operation(summary = "Unblock user", description = "Reactivates a previously blocked user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unblocked successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<User> unblockUser(
            @Parameter(description = "ID of the user to unblock", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.unblockUser(userId));
    }

    // --- Seller Management ---

    @Operation(summary = "List all sellers", description = "Retrieves a paginated list of all sellers and their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sellers retrieved successfully")
    })
    @GetMapping("/sellers")
    public ResponseEntity<Page<Seller>> getAllSellers(
            @Parameter(description = "Filter by seller status")
            @RequestParam(required = false) Seller.Status status,
            Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(sellerService.getSellersByStatus(status, pageable));
        }
        return ResponseEntity.ok(sellerService.getSellersByStatus(null, pageable)); // Fixed: should probably list all if null
    }

    @Operation(summary = "Approve seller", description = "Approves a seller application and enables their store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller approved successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @PutMapping("/sellers/{sellerId}/approve")
    public ResponseEntity<Seller> approveSeller(
            @Parameter(description = "ID of the seller to approve", required = true)
            @PathVariable Long sellerId) {
        return ResponseEntity.ok(sellerService.approveSeller(sellerId));
    }

    @Operation(summary = "Reject seller", description = "Rejects a seller application with a reason")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    @PutMapping("/sellers/{sellerId}/reject")
    public ResponseEntity<Seller> rejectSeller(
            @Parameter(description = "ID of the seller to reject", required = true)
            @PathVariable Long sellerId, @RequestBody RejectRequest request) {
        return ResponseEntity.ok(sellerService.rejectSeller(sellerId, request.getReason()));
    }

    // --- Product Management ---

    @Operation(summary = "List all products", description = "Retrieves a paginated list of all products for moderation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Filter by product status")
            @RequestParam(required = false) Product.Status status,
            Pageable pageable) {
                
        if (status != null) {
            return ResponseEntity.ok(productService.getProductsByStatus(status, pageable));
        }
        return ResponseEntity.ok(productService.getAllProducts(null, pageable));
    }

    @Operation(summary = "Approve product", description = "Approves a product listing for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product approved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/products/{productId}/approve")
    public ResponseEntity<ProductResponse> approveProduct(
            @Parameter(description = "ID of the product to approve", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.approveProduct(productId));
    }

    @Operation(summary = "Block product", description = "Suspends a product listing with a reason")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product blocked successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/products/{productId}/block")
    public ResponseEntity<ProductResponse> blockProduct(
            @Parameter(description = "ID of the product to block", required = true)
            @PathVariable Long productId, @RequestBody BlockRequest request) {
        return ResponseEntity.ok(productService.blockProduct(productId, request.getReason()));
    }

    // --- Order Monitoring ---

    @Operation(summary = "Monitor all orders", description = "Administrative view of all system orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @Parameter(description = "Filter by order status")
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(status, pageable));
    }

    // --- Analytics ---

    @Operation(summary = "Get platform analytics", description = "Retrieves platform-wide summary metrics for a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully")
    })
    @GetMapping("/analytics")
    public ResponseEntity<PlatformAnalyticsResponse> getAnalytics(
            @Parameter(description = "Start date for analytics", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for analytics", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(analyticsService.getPlatformAnalytics(startDate, endDate));
    }
}
