package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.dto.CategoryCreateRequest;
import com.shopbazar.shopbazar.dto.CategoryTreeResponse;
import com.shopbazar.shopbazar.dto.CategoryUpdateRequest;
import com.shopbazar.shopbazar.entity.Category;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Create a category (Root or Sub)
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CategoryCreateRequest request) {
        Category createdCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(ApiResponse.<Category>builder()
                .success(true)
                .message("Category created successfully")
                .data(createdCategory)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.<List<Category>>builder()
                .success(true)
                .message("Successfully retrieved all categories")
                .data(categories)
                .build());
    }

    // 3. Get categories tree hierarchy
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> getCategoryTree() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.<List<CategoryTreeResponse>>builder()
                .success(true)
                .message("Successfully built category hierarchy tree")
                .data(tree)
                .build());
    }

    // 4. Get category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.<Category>builder()
                .success(true)
                .message("Successfully retrieved category")
                .data(category)
                .build());
    }

    // 5. Update a category
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request) {
        Category updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.<Category>builder()
                .success(true)
                .message("Category updated successfully")
                .data(updatedCategory)
                .build());
    }

    // 6. Delete a category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Category deleted successfully")
                .data(null)
                .build());
    }

    // 7. Get category products
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ApiResponse<Page<Product>>> getCategoryProducts(
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = categoryService.getCategoryProducts(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Successfully retrieved products for category")
                .data(products)
                .build());
    }
}
