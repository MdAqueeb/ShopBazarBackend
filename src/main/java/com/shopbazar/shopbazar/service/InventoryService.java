package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.InventoryCreateRequest;
import com.shopbazar.shopbazar.dto.InventoryStockRequest;
import com.shopbazar.shopbazar.entity.Inventory;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.InventoryRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Inventory createInventory(InventoryCreateRequest request) {
        if (request.getStock() == null || request.getStock() < 0) {
            throw new BadRequestException("Initial stock must be 0 or greater");
        }
        
        if (inventoryRepository.existsByProduct_ProductId(request.getProductId())) {
            throw new ConflictException("Inventory already exists for product id: " + request.getProductId());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Inventory inventory = Inventory.builder()
                .product(product)
                .stockQuantity(request.getStock())
                .reservedQuantity(0)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));
    }

    @Transactional
    public Inventory updateInventoryStock(Long productId, InventoryStockRequest request) {
        if (request.getStock() == null || request.getStock() < 0) {
            throw new BadRequestException("Stock must be 0 or greater");
        }

        Inventory inventory = getInventoryByProductId(productId);
        inventory.setStockQuantity(request.getStock());
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public Page<Inventory> getInventories(Long sellerId, Pageable pageable) {
        if (sellerId != null) {
            return inventoryRepository.findByProduct_Seller_SellerId(sellerId, pageable);
        }
        return inventoryRepository.findAll(pageable);
    }

    @Transactional
    public Inventory increaseStock(Long productId, InventoryStockRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Increase quantity must be strictly greater than 0");
        }

        Inventory inventory = getInventoryByProductId(productId);
        inventory.setStockQuantity(inventory.getStockQuantity() + request.getQuantity());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory decreaseStock(Long productId, InventoryStockRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Decrease quantity must be strictly greater than 0");
        }

        Inventory inventory = getInventoryByProductId(productId);
        int availableStock = inventory.getStockQuantity() - inventory.getReservedQuantity();

        if (availableStock < request.getQuantity()) {
            throw new ConflictException("Insufficient available stock to decrease by " + request.getQuantity());
        }

        inventory.setStockQuantity(inventory.getStockQuantity() - request.getQuantity());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory reserveStock(Long productId, InventoryStockRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Reserve quantity must be strictly greater than 0");
        }

        Inventory inventory = getInventoryByProductId(productId);
        int availableStock = inventory.getStockQuantity() - inventory.getReservedQuantity();

        if (availableStock < request.getQuantity()) {
            throw new ConflictException("Insufficient available stock to reserve " + request.getQuantity() + " units");
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory releaseStock(Long productId, InventoryStockRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Release quantity must be strictly greater than 0");
        }

        Inventory inventory = getInventoryByProductId(productId);

        if (inventory.getReservedQuantity() < request.getQuantity()) {
            throw new ConflictException("Cannot release more stock than is currently reserved (" + inventory.getReservedQuantity() + ")");
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() - request.getQuantity());
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public Page<Inventory> getLowStockInventories(Integer threshold, Pageable pageable) {
        if (threshold == null || threshold < 0) {
            throw new BadRequestException("Threshold must be 0 or greater");
        }
        return inventoryRepository.findByStockQuantityLessThanEqual(threshold, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Inventory> getOutOfStockInventories(Pageable pageable) {
        return inventoryRepository.findByStockQuantity(0, pageable);
    }
}
