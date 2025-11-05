package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.member.MemberDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<MemberDto>> register(@Valid @RequestBody MemberDto dto) {
        MemberDto created = memberService.register(dto);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<MemberDto>> get(@PathVariable Long id) {
        MemberDto dto = memberService.find(id);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.OK.getCode(), GeneralSuccessCode.OK.getMessage(), dto);
        return ResponseEntity.ok(payload);
    }
}
