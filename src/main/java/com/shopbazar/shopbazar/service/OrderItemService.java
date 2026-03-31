package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.OrderItemResponse;
import com.shopbazar.shopbazar.entity.OrderItem;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.mapper.DtoMapper;
import com.shopbazar.shopbazar.repository.OrderItemRepository;
import com.shopbazar.shopbazar.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public List<OrderItemResponse> getOrderItemsByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        return orderItemRepository.findByOrder_OrderId(orderId).stream()
                .map(DtoMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    public OrderItemResponse getOrderItemByOrderIdAndId(Long orderId, Long orderItemId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        OrderItem orderItem = orderItemRepository.findByOrder_OrderIdAndOrderItemId(orderId, orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId + " for order: " + orderId));
        return DtoMapper.toOrderItemResponse(orderItem);
    }


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
