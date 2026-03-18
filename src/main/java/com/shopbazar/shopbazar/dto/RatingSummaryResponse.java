package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing rating summary for a product")
public class RatingSummaryResponse {
    @Schema(description = "Product ID", example = "1")
    private Long productId;
    @Schema(description = "Average rating score", example = "4.5")
    private Double averageRating;
    @Schema(description = "Total number of ratings", example = "150")
    private Integer totalRatings;
    @Schema(description = "Breakdown of ratings by count (rating:count)")
    private Map<Integer, Long> ratingBreakdown;
}
