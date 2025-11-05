package com.hexter31376.umc_mission4.dto.review;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDto {
    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;

    @NotNull
    private Integer rating;

    @NotBlank
    private String content;
}

