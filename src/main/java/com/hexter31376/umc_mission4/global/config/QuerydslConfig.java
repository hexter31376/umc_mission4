package com.hexter31376.umc_mission4.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        // Use the Supplier-based constructor to avoid compile-time type mismatch between jakarta/javax EntityManager
        return new JPAQueryFactory((java.util.function.Supplier) () -> em);
    }
}
