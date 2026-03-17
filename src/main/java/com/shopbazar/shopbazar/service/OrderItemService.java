package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.OrderItem;
import com.shopbazar.shopbazar.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public Optional<OrderItem> getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId);
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem updateOrderItem(Long orderItemId, OrderItem orderItem) {
        OrderItem existing = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + orderItemId));
        existing.setOrder(orderItem.getOrder());
        existing.setProduct(orderItem.getProduct());
        existing.setSeller(orderItem.getSeller());
        existing.setQuantity(orderItem.getQuantity());
        existing.setPrice(orderItem.getPrice());
        return orderItemRepository.save(existing);
    }

    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}
