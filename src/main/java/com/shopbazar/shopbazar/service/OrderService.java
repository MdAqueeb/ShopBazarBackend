package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.*;
import com.shopbazar.shopbazar.exception.*;
import com.shopbazar.shopbazar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));

        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + user.getUserId()));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cannot create order with an empty cart");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = Order.builder()
                .user(user)
                .address(address)
                .orderStatus("PLACED")
                .paymentStatus("PENDING")
                .build();

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            // Check inventory
            Inventory inventory = inventoryRepository.findByProduct_ProductId(product.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product: " + product.getName()));

            int availableStock = inventory.getStockQuantity() - inventory.getReservedQuantity();
            if (availableStock < cartItem.getQuantity()) {
                throw new ConflictException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + availableStock + ", Requested: " + cartItem.getQuantity());
            }

            // Update inventory
            inventory.setStockQuantity(inventory.getStockQuantity() - cartItem.getQuantity());
            inventoryRepository.save(inventory);

            // Create OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Record status history
        recordStatusHistory(savedOrder, "PLACED", "Order placed successfully");

        // Clear cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(Long userId, String status, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Page<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByUser_UserIdAndOrderStatus(userId, status, pageable);
        } else {
            orders = orderRepository.findByUser_UserId(userId, pageable);
        }

        return orders.map(this::mapToResponse);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getOrderStatus().equals("PLACED") && !order.getOrderStatus().equals("CONFIRMED")) {
            throw new ConflictException("Order cannot be cancelled in status: " + order.getOrderStatus());
        }

        order.setOrderStatus("CANCELLED");

        // Restore inventory
        for (OrderItem item : order.getOrderItems()) {
            Inventory inventory = inventoryRepository.findByProduct_ProductId(item.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product: " + item.getProduct().getName()));
            inventory.setStockQuantity(inventory.getStockQuantity() + item.getQuantity());
            inventoryRepository.save(inventory);
        }

        Order updatedOrder = orderRepository.save(order);
        recordStatusHistory(updatedOrder, "CANCELLED", reason != null ? reason : "Cancelled by user");

        return mapToResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        recordStatusHistory(updatedOrder, status, "Status updated by admin");

        return mapToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(String status, Pageable pageable) {
        Page<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByOrderStatus(status, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        return orders.map(this::mapToResponse);
    }

    private void recordStatusHistory(Order order, String status, String comment) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .comment(comment)
                .build();
        statusHistoryRepository.save(history);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .addressId(order.getAddress().getAddressId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .orderItems(order.getOrderItems().stream().map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
