package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.dto.ProductCreateRequest;
import com.shopbazar.shopbazar.dto.ProductUpdateRequest;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.ProductImage;
import com.shopbazar.shopbazar.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Create a product
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductCreateRequest request) {
        Product createdProduct = productService.createProduct(request);
        return new ResponseEntity<>(ApiResponse.<Product>builder()
                .success(true)
                .message("Product created successfully")
                .data(createdProduct)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all products with optional category filtering and pagination
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
            @RequestParam(required = false) Long categoryId, Pageable pageable) {
        Page<Product> products = productService.getAllProducts(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .data(products)
                .build());
    }

    // 3. Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.<Product>builder()
                .success(true)
                .message("Product retrieved successfully")
                .data(product)
                .build());
    }

    // 4. Update a product
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long productId, @RequestBody ProductUpdateRequest request) {
        Product updatedProduct = productService.updateProduct(productId, request);
        return ResponseEntity.ok(ApiResponse.<Product>builder()
                .success(true)
                .message("Product updated successfully")
                .data(updatedProduct)
                .build());
    }

    // 5. Delete a product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .build());
    }

    // 6. Search products
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Product>>> searchProducts(
            @RequestParam String keyword, Pageable pageable) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products searched successfully")
                .data(products)
                .build());
    }

    // 7. Get products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<Product>>> getProductsByCategory(
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Category products retrieved successfully")
                .data(products)
                .build());
    }

    // 8. Upload product image
    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductImage>> uploadProductImage(
            @PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        ProductImage uploadedImage = productService.uploadProductImage(productId, file);
        return new ResponseEntity<>(ApiResponse.<ProductImage>builder()
                .success(true)
                .message("Product image uploaded successfully")
                .data(uploadedImage)
                .build(), HttpStatus.CREATED);
    }

    // 9. Get product images
    @GetMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<ProductImage>>> getProductImages(
            @PathVariable Long productId) {
        List<ProductImage> images = productService.getProductImages(productId);
        return ResponseEntity.ok(ApiResponse.<List<ProductImage>>builder()
                .success(true)
                .message("Product images retrieved successfully")
                .data(images)
                .build());
    }

    // 10. Delete product image
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @PathVariable Long productId, @PathVariable Long imageId) {
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Product image deleted successfully")
                .data(null)
                .build());
    }
}
