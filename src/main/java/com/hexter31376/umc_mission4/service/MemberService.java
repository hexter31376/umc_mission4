package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.domain.member.enums.Status;
import com.hexter31376.umc_mission4.dto.member.MemberDto;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto register(MemberDto dto) {
        memberRepository.findByEmail(dto.getEmail()).ifPresent(m -> {
            throw new IllegalArgumentException("Email already exists");
        });
        Member member = Member.builder()
                .email(dto.getEmail())
                .status(dto.getStatus() == null ? Status.ACTIVE : dto.getStatus())
                .build();
        Member saved = memberRepository.save(member);
        return MemberDto.builder().id(saved.getId()).email(saved.getEmail()).status(saved.getStatus()).build();
    }

    public MemberDto find(Long id) {
        Member m = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return MemberDto.builder().id(m.getId()).email(m.getEmail()).status(m.getStatus()).build();
    }
}

