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

    @Operation(
        summary = "회원 가입",
        description = "새로운 회원을 가입시킵니다. 이메일과 회원 상태(ACTIVE, INACTIVE, BANNED, DELETED)를 전송해야 합니다. " +
                     "회원 가입 시 자동으로 장바구니가 생성됩니다."
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<MemberDto>> register(@Valid @RequestBody MemberDto dto) {
        MemberDto created = memberService.register(dto);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "회원 조회",
        description = "회원 ID로 회원 정보를 조회합니다. 회원의 이메일과 상태 정보가 반환됩니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<MemberDto>> get(@PathVariable Long id) {
        MemberDto dto = memberService.find(id);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "회원 정보 수정",
        description = "회원의 상태를 수정합니다. 이메일은 수정할 수 없습니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<MemberDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody MemberDto dto) {
        MemberDto updated = memberService.update(id, dto);
        ApiSuccessResponse<MemberDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            updated
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "회원 삭제",
        description = "회원을 삭제합니다. 회원과 연관된 장바구니, 주문, 리뷰도 함께 삭제될 수 있습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(@PathVariable Long id) {
        memberService.delete(id);
        ApiSuccessResponse<Void> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "회원이 성공적으로 삭제되었습니다.",
            null
        );
        return ResponseEntity.ok(payload);
    }
}
