package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Order;
import com.shopbazar.shopbazar.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long orderId, Order order) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        existing.setTotalAmount(order.getTotalAmount());
        existing.setOrderStatus(order.getOrderStatus());
        existing.setPaymentStatus(order.getPaymentStatus());
        existing.setAddress(order.getAddress());
        return orderRepository.save(existing);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
