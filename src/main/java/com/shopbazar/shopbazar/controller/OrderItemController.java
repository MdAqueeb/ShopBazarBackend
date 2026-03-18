package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.OrderItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Tag(name = "Order Item", description = "Endpoints for managing items within a customer order (Placeholder)")
public class OrderItemController {

    private final OrderItemService orderItemService;
}
