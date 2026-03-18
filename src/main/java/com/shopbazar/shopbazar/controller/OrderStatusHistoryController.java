package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.OrderStatusHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-status-history")
@RequiredArgsConstructor
@Tag(name = "Order Status History", description = "Endpoints for viewing the status transition history of an order (Placeholder)")
public class OrderStatusHistoryController {

    private final OrderStatusHistoryService orderStatusHistoryService;
}
