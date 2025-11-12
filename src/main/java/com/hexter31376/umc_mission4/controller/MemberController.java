package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.member.MemberDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API", description = "회원 가입 및 조회 관련 API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 가입시킵니다. MemberDto 형태로 이메일과 상태를 전송하세요.")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<MemberDto>> register(@Valid @RequestBody MemberDto dto) {
        MemberDto created = memberService.register(dto);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(summary = "회원 조회", description = "ID로 회원 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<MemberDto>> get(@PathVariable Long id) {
        MemberDto dto = memberService.find(id);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.OK.getCode(), GeneralSuccessCode.OK.getMessage(), dto);
        return ResponseEntity.ok(payload);
    }
}
