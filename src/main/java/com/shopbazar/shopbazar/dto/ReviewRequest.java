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
@Schema(description = "Request to submit a product review")
public class ReviewRequest {
    @Schema(description = "ID of the user writing the review", example = "1")
    private Long userId;
    @Schema(description = "Rating given to the product (1-5)", example = "5")
    private Integer rating;
    @Schema(description = "Review comment", example = "Excellent product, highly recommended!")
    private String comment;
}
