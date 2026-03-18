package com.shopbazar.shopbazar.controller;
 
import com.shopbazar.shopbazar.dto.InventoryCreateRequest;
import com.shopbazar.shopbazar.dto.InventoryStockRequest;
import com.shopbazar.shopbazar.entity.Inventory;
import com.shopbazar.shopbazar.service.InventoryService;
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

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Endpoints for managing product stock levels and reservations")
public class InventoryController {

    private final InventoryService inventoryService;

    // 1. Create inventory for a product
    @Operation(summary = "Create inventory", description = "Initializes inventory record for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid inventory data")
    })
    @PostMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> createInventory(@RequestBody InventoryCreateRequest request) {
        Inventory createdInventory = inventoryService.createInventory(request);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory created successfully")
                .data(createdInventory)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get inventory by product id
    @Operation(summary = "Get product inventory", description = "Retrieves current stock levels for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product or inventory not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> getInventoryByProductId(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory retrieved successfully")
                .data(inventory)
                .build());
    }

    // 3. Update inventory stock (Absolute set)
    @Operation(summary = "Update inventory stock", description = "Sets the absolute stock quantity for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> updateInventoryStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.updateInventoryStock(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock updated successfully")
                .data(updatedInventory)
                .build());
    }

    // 4. Get all inventories (Optional filter by sellerId)
    @Operation(summary = "List all inventories", description = "Retrieves a paginated list of all inventory records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Inventory>>> getInventories(
            @Parameter(description = "Optional seller ID filter")
            @RequestParam(required = false) Long sellerId, Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getInventories(sellerId, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Inventories retrieved successfully")
                .data(inventories)
                .build());
    }

    // 5. Increase inventory stock
    @Operation(summary = "Increase stock", description = "Increments the stock quantity for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock increased successfully")
    })
    @PutMapping("/{productId}/increase")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> increaseStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.increaseStock(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock increased successfully")
                .data(updatedInventory)
                .build());
    }

    // 6. Decrease inventory stock
    @Operation(summary = "Decrease stock", description = "Decrements the stock quantity for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock decreased successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock")
    })
    @PutMapping("/{productId}/decrease")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> decreaseStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.decreaseStock(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock decreased successfully")
                .data(updatedInventory)
                .build());
    }

    // 7. Reserve inventory stock
    @Operation(summary = "Reserve stock", description = "Temporarily reserves stock for a product (e.g., during checkout)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock reserved successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock for reservation")
    })
    @PutMapping("/{productId}/reserve")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> reserveStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.reserveStock(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Inventory stock reserved successfully")
                .data(updatedInventory)
                .build());
    }

    // 8. Release reserved stock
    @Operation(summary = "Release reserved stock", description = "Releases previously reserved stock back to available inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserved stock released successfully")
    })
    @PutMapping("/{productId}/release")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Inventory>> releaseStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId, @RequestBody InventoryStockRequest request) {
        Inventory updatedInventory = inventoryService.releaseStock(productId, request);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Inventory>builder()
                .success(true)
                .message("Reserved inventory stock released successfully")
                .data(updatedInventory)
                .build());
    }

    // 9. Get low stock inventories
    @Operation(summary = "Get low stock items", description = "Lists products with stock levels below a specified threshold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock items retrieved successfully")
    })
    @GetMapping("/low-stock")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Inventory>>> getLowStockInventories(
            @Parameter(description = "Stock threshold", required = true)
            @RequestParam Integer threshold, Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getLowStockInventories(threshold, pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Low stock inventories retrieved successfully")
                .data(inventories)
                .build());
    }

    // 10. Get out of stock inventories
    @Operation(summary = "Get out of stock items", description = "Lists products with zero available stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Out of stock items retrieved successfully")
    })
    @GetMapping("/out-of-stock")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Page<Inventory>>> getOutOfStockInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getOutOfStockInventories(pageable);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Page<Inventory>>builder()
                .success(true)
                .message("Out of stock inventories retrieved successfully")
                .data(inventories)
                .build());
    }
}
