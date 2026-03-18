package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.service.OrderItemService;
import com.shopbazar.shopbazar.service.OrderService;
import com.shopbazar.shopbazar.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Endpoints for managing customer orders and transactions")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final TransactionService transactionService;

    @Operation(summary = "Create a new order", description = "Places a new order for the products currently in the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order request or empty cart")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get order by ID", description = "Retrieves details of a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "Get user orders", description = "Retrieves a paginated list of orders for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Filter by order status")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.getUserOrders(userId, status, pageable));
    }

    @Operation(summary = "Cancel order", description = "Cancels an existing order if it's in a cancellable state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled in its current state"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @Parameter(description = "ID of the order to cancel", required = true)
            @PathVariable Long orderId,
            @RequestBody(required = false) OrderCancelRequest request) {
        String reason = (request != null) ? request.getReason() : null;
        return ResponseEntity.ok(orderService.cancelOrder(orderId, reason));
    }

    @Operation(summary = "Update order status", description = "Updates the status of an order (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @Operation(summary = "Get all orders", description = "Retrieves a paginated list of all orders (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @Parameter(description = "Filter by order status")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.getAllOrders(status, pageable));
    }

    @Operation(summary = "Get order items", description = "Retrieves all items belonging to a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/items")
    public ResponseEntity<java.util.List<OrderItemResponse>> getOrderItems(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
    }

    @Operation(summary = "Get single order item", description = "Retrieves a specific item from an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order or item not found")
    })
    @GetMapping("/{orderId}/items/{orderItemId}")
    public ResponseEntity<OrderItemResponse> getOrderItem(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "ID of the order item", required = true)
            @PathVariable Long orderItemId) {
        return ResponseEntity.ok(orderItemService.getOrderItemByOrderIdAndId(orderId, orderItemId));
    }

    @Operation(summary = "Get order transactions", description = "Retrieves all payment transactions associated with an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/transactions")
    public ResponseEntity<java.util.List<com.shopbazar.shopbazar.entity.Transaction>> getOrderTransactions(
            @Parameter(description = "ID of the order", required = true)
            @PathVariable Long orderId) {
        // First check if order exists
        orderService.getOrderById(orderId);
        return ResponseEntity.ok(transactionService.getTransactionsByOrderId(orderId));
    }
}
