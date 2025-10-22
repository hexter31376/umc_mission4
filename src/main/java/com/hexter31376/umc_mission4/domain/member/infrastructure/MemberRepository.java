package com.hexter31376.umc_mission4.domain.member.infrastructure;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);
}
