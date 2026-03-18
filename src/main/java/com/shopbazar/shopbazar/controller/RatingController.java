package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.service.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Rating", description = "Endpoints for submitting and retrieving product star ratings (Placeholder)")
public class RatingController {

    private final RatingService ratingService;
}
