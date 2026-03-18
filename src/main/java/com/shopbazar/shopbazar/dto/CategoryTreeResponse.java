package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response representing a category in a tree structure")
public class CategoryTreeResponse {
    @Schema(description = "Category ID", example = "1")
    private Long categoryId;
    @Schema(description = "Name of the category", example = "Electronics")
    private String name;
    @Schema(description = "List of subcategories")
    private List<CategoryTreeResponse> subCategories;
}
