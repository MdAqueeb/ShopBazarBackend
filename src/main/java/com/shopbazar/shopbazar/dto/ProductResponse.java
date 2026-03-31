package com.shopbazar.shopbazar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// public class ProductResponse {
//     private Long productId;
//     private String name;
//     private String description;
//     private BigDecimal price;
//     private Integer stockQuantity;
//     private String categoryName;
//     private String sellerName;
//     private Long sellerId;
//     private String status;
//     private java.util.List<String> imageUrls;
//     // category 
//     // rating 
//     // reviews
// }

public class ProductResponse {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryName;
    private String sellerName;
    private Long sellerId;
    private String status;
    private List<String> imageUrls;

    private Double rating;
    private Integer reviewCount;
}