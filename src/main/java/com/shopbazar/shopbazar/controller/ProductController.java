package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ProductCreateRequest;
import com.shopbazar.shopbazar.dto.ProductUpdateRequest;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.ProductImage;
import com.shopbazar.shopbazar.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for managing products, including search and image uploads")
public class ProductController {

    private final ProductService productService;

    // 1. Create a product
    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Product>> createProduct(@RequestBody ProductCreateRequest request) {
        Product createdProduct = productService.createProduct(request);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<Product>builder()
                .success(true)
                .message("Product created successfully")
                .data(createdProduct)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all products with optional category filtering and pagination
    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products with optional category filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Product>>> getAllProducts(
            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) Long categoryId, Pageable pageable) {
        Page<Product> products = productService.getAllProducts(categoryId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .data(products)
                .build());
    }

    // 3. Get product by ID
    @Operation(summary = "Get product by ID", description = "Retrieves detailed information for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Product>> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Product>builder()
                .success(true)
                .message("Product retrieved successfully")
                .data(product)
                .build());
    }

    // 4. Update a product
    @Operation(summary = "Update product", description = "Updates details of an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Product>> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long productId, @RequestBody ProductUpdateRequest request) {
        Product updatedProduct = productService.updateProduct(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Product>builder()
                .success(true)
                .message("Product updated successfully")
                .data(updatedProduct)
                .build());
    }

    // 5. Delete a product
    @Operation(summary = "Delete product", description = "Removes a product from the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .build());
    }

    // 6. Search products
    @Operation(summary = "Search products", description = "Searches for products based on a keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Product>>> searchProducts(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword, Pageable pageable) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Products searched successfully")
                .data(products)
                .build());
    }

    // 7. Get products by category
    @Operation(summary = "Get products by category", description = "Retrieves a paginated list of products for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Product>>> getProductsByCategory(
            @Parameter(description = "ID of the category", required = true)
            @PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Product>>builder()
                .success(true)
                .message("Category products retrieved successfully")
                .data(products)
                .build());
    }

    // 8. Upload product image
    @Operation(summary = "Upload product image", description = "Uploads a new image for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{productId}/images")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<ProductImage>> uploadProductImage(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        ProductImage uploadedImage = productService.uploadProductImage(productId, file);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<ProductImage>builder()
                .success(true)
                .message("Product image uploaded successfully")
                .data(uploadedImage)
                .build(), HttpStatus.CREATED);
    }

    // 9. Get product images
    @Operation(summary = "Get product images", description = "Retrieves all images associated with a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}/images")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<List<ProductImage>>> getProductImages(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        List<ProductImage> images = productService.getProductImages(productId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<List<ProductImage>>builder()
                .success(true)
                .message("Product images retrieved successfully")
                .data(images)
                .build());
    }

    // 10. Delete product image
    @Operation(summary = "Delete product image", description = "Removes a specific image from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product or image not found")
    })
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> deleteProductImage(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            @Parameter(description = "ID of the image to delete", required = true)
            @PathVariable Long imageId) {
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Product image deleted successfully")
                .data(null)
                .build());
    }
}
