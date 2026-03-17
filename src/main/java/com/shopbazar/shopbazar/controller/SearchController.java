package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchProducts(
            @RequestParam String keyword, Pageable pageable) {
        Page<Product> products = searchService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchByCategory(
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = searchService.searchByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Category search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Map<String, Object>>> filterProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double rating,
            Pageable pageable) {
        Page<Product> products = searchService.filterProducts(keyword, categoryId, minPrice, maxPrice, rating, pageable);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Filtered search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSuggestions(@RequestParam String keyword) {
        List<String> suggestions = searchService.getSuggestions(keyword);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Suggestions retrieved successfully")
                .data(Map.of("suggestions", suggestions))
                .build());
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTrendingProducts(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Page<Product> products = searchService.getTrendingProducts(limit);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Trending products retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPopularProducts(Pageable pageable) {
        Page<Product> products = searchService.getPopularProducts(pageable);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Popular products retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }
}
