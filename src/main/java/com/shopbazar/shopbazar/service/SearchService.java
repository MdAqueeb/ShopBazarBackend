package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.CategoryRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategory_CategoryId(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> filterProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, Pageable pageable) {
        return productRepository.filterProducts(keyword, categoryId, minPrice, maxPrice, minRating, pageable);
    }

    @Transactional(readOnly = true)
    public List<String> getSuggestions(String keyword) {
        return productRepository.findSuggestions(keyword, PageRequest.of(0, 5));
    }

    @Transactional(readOnly = true)
    public Page<Product> getTrendingProducts(Integer limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        return productRepository.findTrendingProducts(since, PageRequest.of(0, limit != null ? limit : 10));
    }

    @Transactional(readOnly = true)
    public Page<Product> getPopularProducts(Pageable pageable) {
        return productRepository.findPopularProducts(pageable);
    }
}
