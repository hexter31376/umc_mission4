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

    @Operation(
        summary = "리뷰 생성",
        description = "회원이 도서에 대해 리뷰를 작성합니다. 회원 ID, 도서 ID, 평점(1-5), 리뷰 내용을 전송해야 합니다. " +
                     "평점은 1에서 5 사이의 정수여야 하며, 리뷰 내용은 최대 1000자까지 작성할 수 있습니다."
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> create(@Valid @RequestBody ReviewCreateDto dto) {
        ReviewResponseDto created = reviewService.create(dto);
        ApiSuccessResponse<ReviewResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "리뷰 조회",
        description = "리뷰 ID로 리뷰 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> get(@PathVariable Long id) {
        ReviewResponseDto dto = reviewService.find(id);
        ApiSuccessResponse<ReviewResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "리뷰 수정",
        description = "리뷰의 평점과 내용을 수정합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody ReviewCreateDto dto) {
        ReviewResponseDto updated = reviewService.update(id, dto.getRating(), dto.getContent());
        ApiSuccessResponse<ReviewResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "리뷰가 성공적으로 수정되었습니다.",
            updated
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "리뷰 삭제",
        description = "리뷰를 삭제합니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(@PathVariable Long id) {
        reviewService.delete(id);
        ApiSuccessResponse<Void> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "리뷰가 성공적으로 삭제되었습니다.",
            null
        );
        return ResponseEntity.ok(payload);
    }
}
