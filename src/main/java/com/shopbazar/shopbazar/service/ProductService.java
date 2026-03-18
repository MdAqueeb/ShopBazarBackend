package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.ProductCreateRequest;
import com.shopbazar.shopbazar.dto.ProductUpdateRequest;
import com.shopbazar.shopbazar.entity.Category;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.ProductImage;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ForbiddenException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.CategoryRepository;
import com.shopbazar.shopbazar.repository.ProductImageRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import com.shopbazar.shopbazar.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ForbiddenException("Invalid seller profile or not a seller"));
                
        if (seller.getStatus() != Seller.Status.APPROVED) {
            throw new ForbiddenException("Seller account is not approved yet");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .seller(seller)
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus() != null ? request.getStatus() : Product.Status.ACTIVE)
                .build();

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Long categoryId, Pageable pageable) {
        if (categoryId != null) {
            return productRepository.findByCategory_CategoryId(categoryId, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    @Transactional
    public Product updateProduct(Long productId, ProductUpdateRequest request) {
        Product existing = getProductById(productId);
        
        // In a real app we would check if the current SecurityContext user owns this product via seller ID
        // if (!existing.getSeller().getUser().getUserId().equals(currentUser.getId())) throw ForbiddenException(...)

        if (request.getName() != null) existing.setName(request.getName());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());
        if (request.getPrice() != null) existing.setPrice(request.getPrice());
        
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product existing = getProductById(productId);
        productRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategory_CategoryId(categoryId, pageable);
    }

    @Transactional
    public ProductImage uploadProductImage(Long productId, MultipartFile file) {
        Product product = getProductById(productId);
        
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file cannot be empty");
        }

        // Mocking file upload to a cloud bucket / local storage.
        // In a real scenario we'd stream this file to S3 and get the URL back.
        String originalFilename = file.getOriginalFilename();
        String fakeImageUrl = "https://shopbazar-bucket.s3.region.amazonaws.com/products/" 
                              + productId + "/" + UUID.randomUUID() + "-" + originalFilename;

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(fakeImageUrl)
                .build();

        return productImageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public List<ProductImage> getProductImages(Long productId) {
        getProductById(productId); // Ensure product exists
        return productImageRepository.findByProduct_ProductId(productId);
    }

    @Transactional
    public void deleteProductImage(Long productId, Long imageId) {
        getProductById(productId); // Ensure product exists
        
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        if (!image.getProduct().getProductId().equals(productId)) {
            throw new BadRequestException("Image does not belong to the specified product");
        }

        // In a real app we'd also delete the file from the cloud bucket here
        productImageRepository.delete(image);
    }

    @Transactional
    public Product approveProduct(Long productId) {
        Product product = getProductById(productId);
        product.setStatus(Product.Status.ACTIVE);
        product.setBlockReason(null);
        return productRepository.save(product);
    }

    @Transactional
    public Product blockProduct(Long productId, String reason) {
        Product product = getProductById(productId);
        product.setStatus(Product.Status.BLOCKED);
        product.setBlockReason(reason);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByStatus(Product.Status status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }
}
