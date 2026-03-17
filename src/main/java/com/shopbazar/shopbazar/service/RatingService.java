package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Rating;
import com.shopbazar.shopbazar.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Optional<Rating> getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId);
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating updateRating(Long ratingId, Rating rating) {
        Rating existing = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));
        existing.setRating(rating.getRating());
        return ratingRepository.save(existing);
    }

    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}
