package com.hexter31376.umc_mission4;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.domain.member.enums.Status;
import com.hexter31376.umc_mission4.domain.member.infrastructure.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UmcMission4Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(UmcMission4Application.class, args);

        MemberRepository memberRepository = run.getBean(MemberRepository.class);

        Member member = Member.builder()
                .email("123@naver.com")
                .status(Status.ACTIVE)
                .build();

        memberRepository.save(member);

        System.out.println("-------------------");

        Member findMember = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        System.out.println("member = " + findMember);
    }

}
