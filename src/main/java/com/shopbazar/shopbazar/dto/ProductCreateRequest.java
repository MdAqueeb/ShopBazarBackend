package com.shopbazar.shopbazar.dto;

import com.shopbazar.shopbazar.entity.Product;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    private Long sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Product.Status status = Product.Status.ACTIVE;
}
