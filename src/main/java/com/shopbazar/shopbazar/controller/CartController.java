package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.CartItemRequest;
import com.shopbazar.shopbazar.entity.Cart;
import com.shopbazar.shopbazar.entity.CartItem;
import com.shopbazar.shopbazar.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints for managing user shopping carts")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart", description = "Adds a product to the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @PostMapping("/items")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<CartItem>> addItemToCart(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId, @RequestBody CartItemRequest request) {
        CartItem cartItem = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<CartItem>builder()
                .success(true)
                .message("Item added to cart successfully")
                .data(cartItem)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Get user cart", description = "Retrieves the user's shopping cart and its items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Cart>> getCart(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Cart>builder()
                .success(true)
                .message("Cart retrieved successfully")
                .data(cart)
                .build());
    }

    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a specific item in the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<CartItem>> updateCartItemQuantity(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the cart item", required = true)
            @PathVariable Long cartItemId, @RequestBody CartItemRequest request) {
        CartItem cartItem = cartService.updateCartItemQuantity(userId, cartItemId, request.getQuantity());
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<CartItem>builder()
                .success(true)
                .message("Cart item quantity updated successfully")
                .data(cartItem)
                .build());
    }

    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> removeCartItem(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the cart item to remove", required = true)
            @PathVariable Long cartItemId) {
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Item removed from cart successfully")
                .data(null)
                .build());
    }

    @Operation(summary = "Clear cart", description = "Removes all items from the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/clear")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> clearCart(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Cart cleared successfully")
                .data(null)
                .build());
    }
}
