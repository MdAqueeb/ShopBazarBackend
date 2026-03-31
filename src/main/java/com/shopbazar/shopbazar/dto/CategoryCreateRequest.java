package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to create a new category")
public class CategoryCreateRequest {
    @Schema(description = "Name of the category", example = "Electronics")
    private String name;
    @Schema(description = "Parent category ID (null for root)", example = "1")
    private Long parentId; // Optional, null means it's a root category
}
