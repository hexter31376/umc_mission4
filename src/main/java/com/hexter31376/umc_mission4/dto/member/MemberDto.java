package com.hexter31376.umc_mission4.dto.member;

import com.hexter31376.umc_mission4.domain.member.enums.Status;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;

    @Email
    @NotBlank
    private String email;

    private Status status;
}

