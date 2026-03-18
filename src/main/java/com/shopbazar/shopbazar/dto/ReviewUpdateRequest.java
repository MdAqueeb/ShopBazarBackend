package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to update an existing review")
public class ReviewUpdateRequest {
    @Schema(description = "Updated rating (1-5)", example = "4")
    private Integer rating;
    @Schema(description = "Updated review comment", example = "Updated: Still good but slightly expensive.")
    private String comment;
}
