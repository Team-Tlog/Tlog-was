package com.se.Tlog.config.openapi;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition( // Swagger UI 페이지 설정
        info = @Info(
                title = "Tlog API 명세",
                version = "v0.0",
                description = "Tlog 백엔드 서버의 API 명세를 기록한 문서입니다."
                // license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                // contact = @Contact(url = "http://myserver.com", name = "name", email = "name@email.com")
        ),
        servers = {
                @Server(url = "/", description = "서버 선택")
        }
)
@SecurityScheme ( // Swagger UI 페이지 내 인증 기능 설정
		name = "JwtAuthScheme",
		description = "AccessToken을 설정합니다.",
		// paramName = "KeyName", // ApiKey 타입일 경우 설정
		bearerFormat = "JWT",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
	
}
