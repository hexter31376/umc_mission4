package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.review.ReviewCreateDto;
import com.hexter31376.umc_mission4.dto.review.ReviewResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "리뷰 API", description = "리뷰 생성 및 조회 관련 API")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 생성", description = "회원이 도서에 대해 리뷰를 작성합니다. ReviewCreateDto를 전송하세요.")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> create(@Valid @RequestBody ReviewCreateDto dto) {
        ReviewResponseDto created = reviewService.create(dto);
        ApiSuccessResponse<ReviewResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }
}
