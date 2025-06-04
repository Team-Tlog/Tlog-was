package com.se.Tlog.domain.User.controller.dto;

import com.se.Tlog.domain.User.domain.SsoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SSO 인증을 통한 회원가입 요청 DTO")
public record RegisterRequest(
        @Schema(description = "회원가입에 사용할 소셜 인증 타입 (KAKAO, GOOGLE)", example = "KAKAO")
        SsoType type,

        @Schema(description = "소셜 액세스 토큰")
        String accessToken,
        
        @Schema(description = "회원가입시 입력해야하는 사용자 기본 정보입니다.")
        RegisterUserProfileDto userProfile
) {
}
