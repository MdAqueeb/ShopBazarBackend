package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Schema(description = "Request to update an existing product")
public class ProductUpdateRequest {
    @Schema(description = "Updated product name", example = "iPhone 15 Pro Max")
    private String name;
    @Schema(description = "Updated product description", example = "Updated description for iPhone 15 Pro Max")
    private String description;
    @Schema(description = "Updated product price", example = "1099.99")
    private BigDecimal price;
}
