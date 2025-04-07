package com.se.Tlog.domain.Notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Notification.application.NotificationService;
import com.se.Tlog.domain.Notification.controller.dto.AssignTokenRequestDto;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
	
	@PostMapping
	@Operation (
			summary = "FCM Token 등록",
    		description = "사용자 단말기 앱의 고유 FCM Token을 서버에 등록합니다.",
			tags = {"알림"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> assignUserFcmToken(@RequestBody AssignTokenRequestDto request) {
		notificationService.assignFirebaseToken(request);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
}
