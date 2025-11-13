package com.se.Tlog.domain.User.controller;

import com.se.Tlog.domain.User.application.SsoAuthService;
import com.se.Tlog.domain.User.controller.dto.RegisterUserProfileDto;
import com.se.Tlog.domain.User.controller.dto.UserTagRes;
import com.se.Tlog.global.response.success.SuccessRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
// @Profile("dev") // 현재 활성화 기준이 Swagger 임을 감안.
@RequestMapping("/api/test/auth")
@RequiredArgsConstructor
@Tag(name = "SSO Authentication")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class AuthDevController {
    private final SsoAuthService ssoAuthService;

    @PutMapping("/user-tags")
    @Operation(
            summary = "[개발환경 전용] 사용자 정보로부터 사용자의 선호 태그 계산",
            description = "[개발환경 전용] TBTI 코드, 사용자가 선호한 사진을 기반으로 선호 태그 가중치를 계산합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이 사용자에게 적용될 태그 및 가중치"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<List<UserTagRes>>> getUserTagsWithWeight(@RequestBody RegisterUserProfileDto request) {
        RegisterUserProfileDto.validate(request);
        return ResponseEntity.ok(SuccessRes.from(ssoAuthService.getUserTags(request)));
    }
}
