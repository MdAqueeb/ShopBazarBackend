package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.ProductImage;
import com.shopbazar.shopbazar.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImage createProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    public Optional<ProductImage> getProductImageById(Long imageId) {
        return productImageRepository.findById(imageId);
    }

    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public ProductImage updateProductImage(Long imageId, ProductImage productImage) {
        ProductImage existing = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("ProductImage not found with id: " + imageId));
        existing.setImageUrl(productImage.getImageUrl());
        existing.setProduct(productImage.getProduct());
        return productImageRepository.save(existing);
    }

    public void deleteProductImage(Long imageId) {
        productImageRepository.deleteById(imageId);
    }
}
