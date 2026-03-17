package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.CategoryCreateRequest;
import com.shopbazar.shopbazar.dto.CategoryTreeResponse;
import com.shopbazar.shopbazar.dto.CategoryUpdateRequest;
import com.shopbazar.shopbazar.entity.Category;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.InternalServerException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.CategoryRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Category createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ConflictException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(request.getName());

        if (request.getParentId() != null) {
            Category parent = getCategoryById(request.getParentId());
            category.setParentCategory(parent);
        }

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerException("Failed to fetch all categories");
        }
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoryTree() {
        try {
            List<Category> rootCategories = categoryRepository.findByParentCategoryIsNull();
            return rootCategories.stream()
                    .map(this::mapToTreeResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerException("Failed to fetch category hierarchy");
        }
    }

    private CategoryTreeResponse mapToTreeResponse(Category category) {
        List<CategoryTreeResponse> subCategories = null;
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            subCategories = category.getSubCategories().stream()
                    .map(this::mapToTreeResponse)
                    .collect(Collectors.toList());
        }

        return CategoryTreeResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .subCategories(subCategories)
                .build();
    }

    @Transactional
    public Category updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category existing = getCategoryById(categoryId);
        
        if (request.getName() != null && !request.getName().equals(existing.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new ConflictException("Category with name '" + request.getName() + "' already exists");
            }
            existing.setName(request.getName());
        }
        
        return categoryRepository.save(existing);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category existing = getCategoryById(categoryId);
        
        if (existing.getSubCategories() != null && !existing.getSubCategories().isEmpty()) {
            throw new ConflictException("Cannot delete category because it has subcategories attached. Reassign or delete subcategories first.");
        }
        
        if (existing.getProducts() != null && !existing.getProducts().isEmpty()) {
            throw new ConflictException("Cannot delete category because it has products linked to it. Reassign products first.");
        }
        
        categoryRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public Page<Product> getCategoryProducts(Long categoryId, Pageable pageable) {
        getCategoryById(categoryId); // Verify existing
        return productRepository.findByCategory_CategoryId(categoryId, pageable);
    }
}
