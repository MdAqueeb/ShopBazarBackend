package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing review details")
public class ReviewResponse {
    @Schema(description = "Review ID", example = "1")
    private Long reviewId;
    @Schema(description = "User ID of the reviewer", example = "1")
    private Long userId;
    @Schema(description = "Product ID being reviewed", example = "1")
    private Long productId;
    @Schema(description = "Rating given (1-5)", example = "5")
    private Integer rating;
    @Schema(description = "Review comment", example = "Great quality!")
    private String comment;
    @Schema(description = "Review creation timestamp", example = "2024-03-22T08:30:00")
    private LocalDateTime createdAt;
}
