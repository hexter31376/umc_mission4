package com.hexter31376.umc_mission4.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long memberId;
    private String email;
    private String role;
}

