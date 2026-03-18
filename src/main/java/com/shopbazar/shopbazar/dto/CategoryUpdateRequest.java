package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to update an existing category")
public class CategoryUpdateRequest {
    @Schema(description = "New name for the category", example = "Home Appliances")
    private String name;
}
