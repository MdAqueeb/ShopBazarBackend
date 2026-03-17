package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.dto.InventoryCreateRequest;
import com.shopbazar.shopbazar.dto.InventoryStockRequest;
import com.shopbazar.shopbazar.entity.Inventory;
import com.shopbazar.shopbazar.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // 1. Create inventory for a product
    @PostMapping
    public ResponseEntity<ApiResponse<Inventory>> createInventory(@RequestBody InventoryCreateRequest request) {
        Inventory createdInventory = inventoryService.createInventory(request);
        return new ResponseEntity<>(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory created successfully")
                .data(createdInventory)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get inventory by product id
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Inventory>> getInventoryByProductId(@PathVariable Long productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory retrieved successfully")
                .data(inventory)
                .build());
    }

    // 3. Update inventory stock (Absolute set)
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Inventory>> updateInventoryStock(
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.updateInventoryStock(productId, request);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock updated successfully")
                .data(updatedInventory)
                .build());
    }

    // 4. Get all inventories (Optional filter by sellerId)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Inventory>>> getInventories(
            @RequestParam(required = false) Long sellerId, Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getInventories(sellerId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Inventories retrieved successfully")
                .data(inventories)
                .build());
    }

    // 5. Increase inventory stock
    @PutMapping("/{productId}/increase")
    public ResponseEntity<ApiResponse<Inventory>> increaseStock(
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.increaseStock(productId, request);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock increased successfully")
                .data(updatedInventory)
                .build());
    }

    // 6. Decrease inventory stock
    @PutMapping("/{productId}/decrease")
    public ResponseEntity<ApiResponse<Inventory>> decreaseStock(
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.decreaseStock(productId, request);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock decreased successfully")
                .data(updatedInventory)
                .build());
    }

    // 7. Reserve inventory stock
    @PutMapping("/{productId}/reserve")
    public ResponseEntity<ApiResponse<Inventory>> reserveStock(
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.reserveStock(productId, request);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock reserved successfully")
                .data(updatedInventory)
                .build());
    }

    // 8. Release reserved stock
    @PutMapping("/{productId}/release")
    public ResponseEntity<ApiResponse<Inventory>> releaseStock(
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.releaseStock(productId, request);
        return ResponseEntity.ok(ApiResponse.<Inventory>builder()
                .success(true)
                .message("Reserved inventory stock released successfully")
                .data(updatedInventory)
                .build());
    }

    // 9. Get low stock inventories
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<Page<Inventory>>> getLowStockInventories(
            @RequestParam Integer threshold, Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getLowStockInventories(threshold, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Low stock inventories retrieved successfully")
                .data(inventories)
                .build());
    }

    // 10. Get out of stock inventories
    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<Page<Inventory>>> getOutOfStockInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getOutOfStockInventories(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Out of stock inventories retrieved successfully")
                .data(inventories)
                .build());
    }
}
