package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.ProductCreateRequest;
import com.shopbazar.shopbazar.dto.ProductImageResponse;
import com.shopbazar.shopbazar.dto.ProductResponse;
import com.shopbazar.shopbazar.dto.ProductUpdateRequest;
import com.shopbazar.shopbazar.entity.Category;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.ProductImage;
import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ForbiddenException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.mapper.DtoMapper;
import com.shopbazar.shopbazar.repository.CategoryRepository;
import com.shopbazar.shopbazar.repository.ProductImageRepository;
import com.shopbazar.shopbazar.repository.ProductRepository;
import com.shopbazar.shopbazar.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
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

        return DtoMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Long categoryId, Pageable pageable) {
        Page<Product> productPage;
        if (categoryId != null) {
            productPage = productRepository.findByCategory_CategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage.map(DtoMapper::toProductResponse);
    }

    //  @Transactional(readOnly = true)
    // // public Page<Product> getAllProducts(Long categoryId, Pageable pageable) {
    // public Page<ProductResponse> getAllProducts(Long categoryId, Pageable pageable) {
    //     Page<ProductResponse> productPage;
    //     if (categoryId != null) {
    //         // return productRepository.findByCategory_CategoryId(categoryId, pageable);
    //         productPage = productRepository.findByCategory_CategoryId(categoryId, pageable);
    //     } else {
    //         productPage = productRepository.findAll(pageable);
    //     }
    //     // return productRepository.findAll(pageable);
    //     return productPage.map(DtoMapper::toProductResponse);
    // }


    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(DtoMapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if (request.getName() != null) existing.setName(request.getName());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());
        if (request.getPrice() != null) existing.setPrice(request.getPrice());
        
        return DtoMapper.toProductResponse(productRepository.save(existing));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductResponse existing = getProductById(productId);
        productRepository.deleteById(existing.getProductId());
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
                .map(DtoMapper::toProductResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategory_CategoryId(categoryId, pageable)
                .map(DtoMapper::toProductResponse);
    }

    @Transactional
    public ProductImageResponse uploadProductImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String fakeImageUrl = "https://shopbazar-bucket.s3.region.amazonaws.com/products/" 
                              + productId + "/" + UUID.randomUUID() + "-" + originalFilename;

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(fakeImageUrl)
                .build();

        return DtoMapper.toProductImageResponse(productImageRepository.save(image));
    }

    @Transactional(readOnly = true)
    public List<ProductImageResponse> getProductImages(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return productImageRepository.findByProduct_ProductId(productId).stream()
                .map(DtoMapper::toProductImageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductImage(Long productId, Long imageId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        if (!image.getProduct().getProductId().equals(productId)) {
            throw new BadRequestException("Image does not belong to the specified product");
        }

        productImageRepository.delete(image);
    }

    @Transactional
    public ProductResponse approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        product.setStatus(Product.Status.ACTIVE);
        product.setBlockReason(null);
        return DtoMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse blockProduct(Long productId, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        product.setStatus(Product.Status.BLOCKED);
        product.setBlockReason(reason);
        return DtoMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByStatus(Product.Status status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable)
                .map(DtoMapper::toProductResponse);
    }
}
