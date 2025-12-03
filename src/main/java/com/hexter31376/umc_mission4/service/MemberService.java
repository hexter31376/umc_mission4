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
                .password(dto.getPassword() != null ? dto.getPassword() : "defaultPassword") // 기본값 설정
                .status(dto.getStatus() == null ? Status.ACTIVE : dto.getStatus())
                .role(dto.getRole() != null ? dto.getRole() : "ROLE_USER")
                .build();
        Member saved = memberRepository.save(member);
        return MemberDto.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .status(saved.getStatus())
                .role(saved.getRole())
                .build();
    }

    public MemberDto find(Long id) {
        Member m = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return MemberDto.builder()
                .id(m.getId())
                .email(m.getEmail())
                .status(m.getStatus())
                .role(m.getRole())
                .build();
    }

    public MemberDto update(Long id, MemberDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));

        if (dto.getStatus() != null) {
            member.updateStatus(dto.getStatus());
        }

        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .status(member.getStatus())
                .role(member.getRole())
                .build();
    }

    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new EntityNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }
}

