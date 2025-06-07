package com.se.Tlog.domain.Social.Profile.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Social.Profile.application.SnsProfileService;
import com.se.Tlog.domain.Social.Profile.controller.dto.SnsProfileRes;
import com.se.Tlog.domain.Social.Profile.controller.dto.UpdateSnsDescriptionReq;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.security.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/sns/profile")
@RequiredArgsConstructor
@Tag(name = "SNS 프로필")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class SnsProfileController {
    private final SnsProfileService snsProfileService;
    
    @PostMapping("/sns-description")
    @Operation (
            summary = "SNS 프로필의 한 줄 설명글 변경",
            description = "SNS 프로필의 한 줄 설명글을 변경합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<?>> updateSnsDescription(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UpdateSnsDescriptionReq request) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        
        snsProfileService.updateSnsDescription(UUID.fromString(user.getId()), request);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
    
    @GetMapping
    @Operation (
            summary = "SNS 프로필 조회",
            description = "사용자의 SNS 프로필을 조회합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<SnsProfileRes>> getSnsProfile(
            @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        
        return ResponseEntity.ok(SuccessRes.from(
                snsProfileService.getSnsProfile((UUID.fromString(user.getId())))));
    }
}
