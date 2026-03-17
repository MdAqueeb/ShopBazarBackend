package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Cart;
import com.shopbazar.shopbazar.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(Long cartId) {
        return cartRepository.findById(cartId);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart updateCart(Long cartId, Cart cart) {
        Cart existing = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        existing.setUser(cart.getUser());
        existing.setCartItems(cart.getCartItems());
        return cartRepository.save(existing);
    }

    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
