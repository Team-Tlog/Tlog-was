package com.se.Tlog.domain.User.controller.dto;

import com.se.Tlog.domain.User.domain.SsoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SSO 로그인 요청 DTO")
public record LoginRequest(
        @Schema(description = "로그인할 소셜 타입 (KAKAO, GOOGLE)", example = "KAKAO")
        SsoType type,

        @Schema(description = "소셜 액세스 토큰")
        String accessToken
) {
}
