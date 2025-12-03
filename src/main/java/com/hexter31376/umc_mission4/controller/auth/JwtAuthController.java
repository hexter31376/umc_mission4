package com.hexter31376.umc_mission4.controller.auth;

import com.hexter31376.umc_mission4.config.security.JwtTokenProvider;
import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.domain.member.enums.Status;
import com.hexter31376.umc_mission4.dto.member.AuthResponse;
import com.hexter31376.umc_mission4.dto.member.LoginRequest;
import com.hexter31376.umc_mission4.dto.member.RegisterRequest;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt/auth")
@RequiredArgsConstructor
@Tag(name = "JWT 인증 API", description = "JWT 기반 회원가입, 로그인 API")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "JWT 회원가입",
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

        String token = tokenProvider.generateTokenFromEmail(savedMember.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
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
            summary = "JWT 로그인",
            description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다. " +
                    "발급받은 토큰은 Authorization 헤더에 'Bearer {token}' 형식으로 포함하여 요청합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .memberId(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .build();

        ApiSuccessResponse<AuthResponse> response = new ApiSuccessResponse<>(
                GeneralSuccessCode.OK.getCode(),
                "로그인에 성공했습니다.",
                authResponse
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "현재 로그인한 사용자 정보 조회",
            description = "JWT 토큰을 통해 현재 인증된 사용자의 정보를 조회합니다. " +
                    "Authorization 헤더에 'Bearer {token}'을 포함해야 합니다."
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
}

