package com.hexter31376.umc_mission4.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UMC 미션4 API")
                        .version("v1")
                        .description("이 API는 도서 판매/장바구니/주문/리뷰 관리를 위한 REST API 문서입니다. 모든 설명은 한국어로 제공됩니다.")
                )
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("기본 로컬 서버")
                ));
    }
}

