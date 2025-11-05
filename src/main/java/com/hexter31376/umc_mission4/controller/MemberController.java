package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.member.MemberDto;
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
    public ResponseEntity<MemberDto> register(@Valid @RequestBody MemberDto dto) {
        MemberDto created = memberService.register(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.find(id));
    }
}

