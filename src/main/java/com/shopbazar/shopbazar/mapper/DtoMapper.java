package com.shopbazar.shopbazar.mapper;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.*;
import java.util.stream.Collectors;

public class DtoMapper {

    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) return null;
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getInventory() != null ? product.getInventory().getStockQuantity() : 0)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .sellerName(product.getSeller() != null ? product.getSeller().getStoreName() : null)
                .sellerId(product.getSeller() != null ? product.getSeller().getSellerId() : null)
                .status(product.getStatus().name())
                .imageUrls(product.getImages() != null ? product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static CategoryTreeResponse toCategoryResponse(Category category) {
        if (category == null) return null;
        return CategoryTreeResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .subCategories(category.getSubCategories() != null ? 
                    category.getSubCategories().stream().map(DtoMapper::toCategoryResponse).collect(Collectors.toList()) : null)
                .build();
    }

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .addressId(order.getAddress() != null ? order.getAddress().getAddressId() : null)
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .orderItems(order.getOrderItems() != null ? order.getOrderItems().stream()
                        .map(DtoMapper::toOrderItemResponse)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem item) {
        if (item == null) return null;
        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public static CartResponse toCartResponse(Cart cart) {
        if (cart == null) return null;
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .items(cart.getCartItems().stream()
                        .map(DtoMapper::toCartItemResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public static CartItemResponse toCartItemResponse(CartItem item) {
        if (item == null) return null;
        return CartItemResponse.builder()
                .cartItemId(item.getCartItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getProduct().getPrice())
                .build();
    }

    public static SellerResponseDTO toSellerResponse(Seller seller) {
        if (seller == null) return null;
        return SellerResponseDTO.builder()
                .sellerId(seller.getSellerId())
                .storeName(seller.getStoreName())
                .status(seller.getStatus().name())
                .build();
    }

    public static ProductImageResponse toProductImageResponse(ProductImage image) {
        if (image == null) return null;
        return ProductImageResponse.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .productId(image.getProduct().getProductId())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        if (transaction == null) return null;
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .orderId(transaction.getOrder().getOrderId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().name())
                .gatewayName(transaction.getGatewayName())
                // .createdAt(transaction.getCreatedAt())
                .build();
    }
}
