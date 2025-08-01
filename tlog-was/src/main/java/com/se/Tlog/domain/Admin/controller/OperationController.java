package com.se.Tlog.domain.Admin.controller;

import com.se.Tlog.domain.Admin.application.OperationService;
import com.se.Tlog.domain.Admin.controller.dto.CreateFeedbackReq;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/operation")
@RequiredArgsConstructor
@Tag(name = "서비스 운영 관련")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class OperationController {
    private final OperationService operationService;

    @PostMapping("/feedback")
    @Operation(
            summary = "개발자에게 피드백",
            description = "피드백을 전달합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 별도로 반환되는 정보는 없습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<?>> makeFeedback(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CreateFeedbackReq request) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        operationService.makeFeedback(UUID.fromString(user.getId()), request);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
}
