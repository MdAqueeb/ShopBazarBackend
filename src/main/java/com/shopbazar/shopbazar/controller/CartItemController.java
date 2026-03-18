package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.CartItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
@Tag(name = "Cart Item", description = "Endpoints for managing individual items within a shopping cart (Placeholder)")
public class CartItemController {

    private final CartItemService cartItemService;
}
