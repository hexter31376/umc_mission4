package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.review.ReviewCreateDto;
import com.hexter31376.umc_mission4.dto.review.ReviewResponseDto;
import com.hexter31376.umc_mission4.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@Valid @RequestBody ReviewCreateDto dto) {
        return ResponseEntity.ok(reviewService.create(dto));
    }
}
