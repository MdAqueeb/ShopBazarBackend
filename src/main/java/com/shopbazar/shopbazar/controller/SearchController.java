package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Search", description = "Endpoints for product search, filtering, category-based lookup, and suggestions")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search products", description = "Finds products matching a specific keyword in title or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> searchProducts(
            @Parameter(description = "Keyword to search for", required = true)
            @RequestParam String keyword, Pageable pageable) {
        Page<Product> products = searchService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @Operation(summary = "Search by category", description = "Retrieves all products belonging to a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category search results retrieved successfully")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> searchByCategory(
            @Parameter(description = "ID of the category", required = true)
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = searchService.searchByCategory(categoryId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Category search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @Operation(summary = "Filter products", description = "Advanced search with multiple filters like price range, rating, and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered results retrieved successfully")
    })
    @GetMapping("/filter")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> filterProducts(
            @Parameter(description = "Optional search keyword")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Optional category ID")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Minimum rating filter")
            @RequestParam(required = false) Double rating,
            Pageable pageable) {
        Page<Product> products = searchService.filterProducts(keyword, categoryId, minPrice, maxPrice, rating, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Filtered search results retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @Operation(summary = "Get search suggestions", description = "Provides a list of suggested keywords based on partial input")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggestions retrieved successfully")
    })
    @GetMapping("/suggestions")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> getSuggestions(
            @Parameter(description = "Partial keyword to get suggestions for", required = true)
            @RequestParam String keyword) {
        List<String> suggestions = searchService.getSuggestions(keyword);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Suggestions retrieved successfully")
                .data(Map.of("suggestions", suggestions))
                .build());
    }

    @Operation(summary = "Get trending products", description = "Lists the currently most-viewed or trending products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trending products retrieved successfully")
    })
    @GetMapping("/trending")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> getTrendingProducts(
            @Parameter(description = "Maximum number of trending items to return")
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Page<Product> products = searchService.getTrendingProducts(limit);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Trending products retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }

    @Operation(summary = "Get popular products", description = "Lists products with the highest sales or ratings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Popular products retrieved successfully")
    })
    @GetMapping("/popular")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Map<String, Object>>> getPopularProducts(Pageable pageable) {
        Page<Product> products = searchService.getPopularProducts(pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Popular products retrieved successfully")
                .data(Map.of("products", products.getContent()))
                .build());
    }
}
