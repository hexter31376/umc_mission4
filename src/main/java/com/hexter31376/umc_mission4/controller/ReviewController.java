package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.review.ReviewCreateDto;
import com.hexter31376.umc_mission4.dto.review.ReviewResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
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
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> create(@Valid @RequestBody ReviewCreateDto dto) {
        ReviewResponseDto created = reviewService.create(dto);
        ApiSuccessResponse<ReviewResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }
}
