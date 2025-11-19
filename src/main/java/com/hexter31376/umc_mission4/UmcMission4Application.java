package com.hexter31376.umc_mission4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UmcMission4Application {

    public static void main(String[] args) {
        SpringApplication.run(UmcMission4Application.class, args);
    }

}
