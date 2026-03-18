package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.RatingSummaryResponse;
import com.shopbazar.shopbazar.dto.ReviewRequest;
import com.shopbazar.shopbazar.dto.ReviewResponse;
import com.shopbazar.shopbazar.dto.ReviewUpdateRequest;
import com.shopbazar.shopbazar.entity.Product;
import com.shopbazar.shopbazar.entity.Review;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.ProductRepository;
import com.shopbazar.shopbazar.repository.ReviewRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse addReview(Long productId, ReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (reviewRepository.existsByProduct_ProductIdAndUser_UserId(productId, request.getUserId())) {
            throw new ConflictException("User has already reviewed this product");
        }

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProduct_ProductId(productId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public RatingSummaryResponse getRatingSummary(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        List<Review> reviews = reviewRepository.findByProduct_ProductId(productId);
        int totalRatings = reviews.size();
        
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Map<Integer, Long> breakdown = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            breakdown.put(i, 0L);
        }
        
        reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()))
                .forEach(breakdown::put);

        return RatingSummaryResponse.builder()
                .productId(productId)
                .averageRating(averageRating)
                .totalRatings(totalRatings)
                .ratingBreakdown(breakdown)
                .build();
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review updatedReview = reviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .productId(review.getProduct().getProductId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
