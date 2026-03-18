package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.ProductImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-images")
@RequiredArgsConstructor
@Tag(name = "Product Image", description = "Endpoints for managing product gallery and thumbnail images (Placeholder)")
public class ProductImageController {

    private final ProductImageService productImageService;
}
