package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.dto.RatingSummaryResponse;
import com.shopbazar.shopbazar.dto.ReviewRequest;
import com.shopbazar.shopbazar.dto.ReviewResponse;
import com.shopbazar.shopbazar.dto.ReviewUpdateRequest;
import com.shopbazar.shopbazar.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Endpoints for managing product reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Add a review", description = "User adds a rating and comment for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "404", description = "Product or user not found")
    })
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            @RequestBody ReviewRequest request) {
        return new ResponseEntity<>(reviewService.addReview(productId, request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get product reviews", description = "Retrieves a paginated list of reviews for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    })
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId, pageable));
    }

    @Operation(summary = "Get rating summary", description = "Retrieves an aggregated rating summary for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating summary retrieved successfully")
    })
    @GetMapping("/products/{productId}/ratings")
    public ResponseEntity<RatingSummaryResponse> getRatingSummary(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getRatingSummary(productId));
    }

    @Operation(summary = "Update review", description = "Updates an existing review's rating or comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "ID of the review to update", required = true)
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));
    }

    @Operation(summary = "Delete review", description = "Removes a review from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID of the review to delete", required = true)
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
