package com.hexter31376.umc_mission4.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "UMC 미션4 API",
                version = "v1",
                description = "이 API는 도서 판매/장바구니/주문/리뷰 관리를 위한 REST API 문서입니다. 모든 설명은 한국어로 제공됩니다.",
                contact = @Contact(name = "개발자", email = "dev@example.com"),
                license = @License(name = "MIT")
        ),
        servers = {@Server(url = "/", description = "기본 로컬 서버")}
)
@Configuration
public class OpenApiConfig {
    // 빈 설정 파일 - 기본 설정만으로 Swagger UI가 활성화됩니다.
}

