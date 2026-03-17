package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.dto.CartItemRequest;
import com.shopbazar.shopbazar.entity.Cart;
import com.shopbazar.shopbazar.entity.CartItem;
import com.shopbazar.shopbazar.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItem>> addItemToCart(
            @PathVariable Long userId, @RequestBody CartItemRequest request) {
        CartItem cartItem = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(ApiResponse.<CartItem>builder()
                .success(true)
                .message("Item added to cart successfully")
                .data(cartItem)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .message("Cart retrieved successfully")
                .data(cart)
                .build());
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItemQuantity(
            @PathVariable Long userId, @PathVariable Long cartItemId, @RequestBody CartItemRequest request) {
        CartItem cartItem = cartService.updateCartItemQuantity(userId, cartItemId, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.<CartItem>builder()
                .success(true)
                .message("Cart item quantity updated successfully")
                .data(cartItem)
                .build());
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(
            @PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Item removed from cart successfully")
                .data(null)
                .build());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Cart cleared successfully")
                .data(null)
                .build());
    }
}
