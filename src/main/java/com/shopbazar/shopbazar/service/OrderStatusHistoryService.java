package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.OrderStatusHistory;
import com.shopbazar.shopbazar.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderStatusHistory createOrderStatusHistory(OrderStatusHistory history) {
        return orderStatusHistoryRepository.save(history);
    }

    public Optional<OrderStatusHistory> getOrderStatusHistoryById(Long historyId) {
        return orderStatusHistoryRepository.findById(historyId);
    }

    public List<OrderStatusHistory> getAllOrderStatusHistories() {
        return orderStatusHistoryRepository.findAll();
    }

    public OrderStatusHistory updateOrderStatusHistory(Long historyId, OrderStatusHistory history) {
        OrderStatusHistory existing = orderStatusHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("OrderStatusHistory not found with id: " + historyId));
        existing.setStatus(history.getStatus());
        return orderStatusHistoryRepository.save(existing);
    }

    public void deleteOrderStatusHistory(Long historyId) {
        orderStatusHistoryRepository.deleteById(historyId);
    }
}
