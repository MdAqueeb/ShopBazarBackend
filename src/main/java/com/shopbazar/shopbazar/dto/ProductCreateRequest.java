package com.shopbazar.shopbazar.dto;

import com.shopbazar.shopbazar.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request to create a new product")
public class ProductCreateRequest {
    @Schema(description = "Seller ID", example = "1")
    private Long sellerId;
    @Schema(description = "Product name", example = "iPhone 15 Pro")
    private String name;
    @Schema(description = "Product description", example = "Latest Apple iPhone with Titanium design")
    private String description;
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;
    @Schema(description = "Category ID", example = "2")
    private Long categoryId;
    @Schema(description = "Product status", example = "ACTIVE")
    private Product.Status status = Product.Status.ACTIVE;
}
