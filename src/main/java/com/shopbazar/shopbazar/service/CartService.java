package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.CartItemRequest;
import com.shopbazar.shopbazar.entity.*;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.ForbiddenException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public CartItem addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Check stock availability
        Inventory inventory = inventoryRepository.findByProduct_ProductId(product.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + product.getName()));

        int availableStock = inventory.getStockQuantity() - inventory.getReservedQuantity();
        if (availableStock < request.getQuantity()) {
            throw new ConflictException("Insufficient stock. Only " + availableStock + " units available.");
        }

        // Check if item already exists in cart
        return cartItemRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), product.getProductId())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                    return cartItemRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .product(product)
                            .quantity(request.getQuantity())
                            .build();
                    return cartItemRepository.save(newItem);
                });
    }

    @Transactional(readOnly = true)
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this cart item.");
        }

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero.");
        }

        // Check stock availability
        Inventory inventory = inventoryRepository.findByProduct_ProductId(cartItem.getProduct().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + cartItem.getProduct().getName()));

        int availableStock = inventory.getStockQuantity() - inventory.getReservedQuantity();
        if (availableStock < quantity) {
            throw new ConflictException("Insufficient stock. Only " + availableStock + " units available.");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to remove this cart item.");
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .cartItems(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}
