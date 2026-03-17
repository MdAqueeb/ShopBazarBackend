package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.CartItem;
import com.shopbazar.shopbazar.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem updateCartItem(Long cartItemId, CartItem cartItem) {
        CartItem existing = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));
        existing.setCart(cartItem.getCart());
        existing.setProduct(cartItem.getProduct());
        existing.setQuantity(cartItem.getQuantity());
        return cartItemRepository.save(existing);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
