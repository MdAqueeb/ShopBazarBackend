package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.CategoryCreateRequest;
import com.shopbazar.shopbazar.dto.CategoryTreeResponse;
import com.shopbazar.shopbazar.dto.CategoryUpdateRequest;
import com.shopbazar.shopbazar.entity.Category;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "Endpoints for managing product categories and hierarchy")
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Create a category (Root or Sub)
    @Operation(summary = "Create a category", description = "Creates a new root or sub-category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    @PostMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Category>> createCategory(@RequestBody CategoryCreateRequest request) {
        Category createdCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<Category>builder()
                .success(true)
                .message("Category created successfully")
                .data(createdCategory)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all categories
    @Operation(summary = "Get all categories", description = "Retrieves a flat list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<List<Category>>builder()
                .success(true)
                .message("Successfully retrieved all categories")
                .data(categories)
                .build());
    }

    // 3. Get categories tree hierarchy
    @Operation(summary = "Get category tree", description = "Retrieves categories in a hierarchical tree structure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully built category hierarchy tree")
    })
    @GetMapping("/tree")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<List<CategoryTreeResponse>>> getCategoryTree() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<List<CategoryTreeResponse>>builder()
                .success(true)
                .message("Successfully built category hierarchy tree")
                .data(tree)
                .build());
    }

    // 4. Get category by ID
    @Operation(summary = "Get category by ID", description = "Retrieves a single category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Category>> getCategoryById(
            @Parameter(description = "ID of the category", required = true)
            @PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Category>builder()
                .success(true)
                .message("Successfully retrieved category")
                .data(category)
                .build());
    }

    // 5. Update a category
    @Operation(summary = "Update a category", description = "Updates details of an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Category>> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request) {
        Category updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Category>builder()
                .success(true)
                .message("Category updated successfully")
                .data(updatedCategory)
                .build());
    }

    // 6. Delete a category
    @Operation(summary = "Delete a category", description = "Removes a category from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Category deleted successfully")
                .data(null)
                .build());
    }

    // 7. Get category products
    @Operation(summary = "Get category products", description = "Retrieves a paginated list of products for a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products for category"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Product>>> getCategoryProducts(
            @Parameter(description = "ID of the category", required = true)
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = categoryService.getCategoryProducts(categoryId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Successfully retrieved products for category")
                .data(products)
                .build());
    }
}
