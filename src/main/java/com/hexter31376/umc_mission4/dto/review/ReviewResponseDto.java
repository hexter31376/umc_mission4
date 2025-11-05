package com.hexter31376.umc_mission4.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id;
    private Long bookId;
    private Long memberId;
    private Integer rating;
    private String content;
}

