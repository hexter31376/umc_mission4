package com.hexter31376.umc_mission4.controller.auth;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.domain.member.enums.Status;
import com.hexter31376.umc_mission4.dto.member.AuthResponse;
import com.hexter31376.umc_mission4.dto.member.RegisterRequest;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session/auth")
@RequiredArgsConstructor
@Tag(name = "세션 인증 API", description = "세션 기반 회원가입, 로그인, 로그아웃 API")
public class SessionAuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "세션 회원가입",
            description = "새로운 회원을 등록합니다. 비밀번호는 암호화되어 저장되며, 기본 권한은 ROLE_USER입니다."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(request.getStatus() != null ? request.getStatus() : Status.ACTIVE)
                .role("ROLE_USER")
                .build();

        Member savedMember = memberRepository.save(member);

        AuthResponse authResponse = AuthResponse.builder()
                .memberId(savedMember.getId())
                .email(savedMember.getEmail())
                .role(savedMember.getRole())
                .build();

        ApiSuccessResponse<AuthResponse> response = new ApiSuccessResponse<>(
                GeneralSuccessCode.CREATED.getCode(),
                "회원가입이 완료되었습니다.",
                authResponse
        );

        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(response);
    }

    @Operation(
            summary = "현재 로그인한 사용자 정보 조회 (세션)",
            description = "세션을 통해 현재 인증된 사용자의 정보를 조회합니다. " +
                    "로그인 후 세션 쿠키가 자동으로 관리됩니다."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<AuthResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        AuthResponse authResponse = AuthResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .build();

        ApiSuccessResponse<AuthResponse> response = new ApiSuccessResponse<>(
                GeneralSuccessCode.OK.getCode(),
                "사용자 정보 조회 성공",
                authResponse
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "세션 로그인 상태 확인",
            description = "현재 세션이 유효한지 확인합니다."
    )
    @GetMapping("/check")
    public ResponseEntity<ApiSuccessResponse<String>> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                    GeneralSuccessCode.OK.getCode(),
                    "세션이 유효합니다.",
                    "로그인된 사용자: " + authentication.getName()
            );
            return ResponseEntity.ok(response);
        }

        ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                GeneralSuccessCode.OK.getCode(),
                "세션이 유효하지 않습니다.",
                "로그인이 필요합니다."
        );
        return ResponseEntity.ok(response);
    }
}

