package com.se.Tlog.domain.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.se.Tlog.domain.User.application.AuthenticationService;
import com.se.Tlog.domain.User.application.SsoAuthService;
import com.se.Tlog.domain.User.controller.dto.LoginRequest;
import com.se.Tlog.domain.User.controller.dto.SsoLoginRequest;
import com.se.Tlog.domain.User.controller.dto.TokenDto;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authService;
	private final SsoAuthService ssoService;

	@GetMapping("/sso/login/kakao")
	public ResponseEntity<SuccessRes<SsoLoginRequest>> getSsoLoginByKakao() {
		return ResponseEntity.ok(
				SuccessRes.from(authService.getSsoLoginRequest(SsoType.KAKAO)));
	}

	@GetMapping("/sso/login/google")
	public ResponseEntity<SuccessRes<SsoLoginRequest>> getSsoLoginByGoogle() {
		return ResponseEntity.ok(
				SuccessRes.from(authService.getSsoLoginRequest(SsoType.GOOGLE)));
	}

	@GetMapping("/sso/callback/kakao")
	public ResponseEntity<SuccessRes<?>> getSsoCallbackByKakao(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.KAKAO, code, error);
	}

	@GetMapping("/sso/callback/google")
	public ResponseEntity<SuccessRes<?>> getSsoCallbackByGoogle(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.GOOGLE, code, error);
	}
	
	private ResponseEntity<SuccessRes<?>> ssoCallBack(SsoType type, String code, String error) {
		if (error != null)
			throw new CustomException(ErrorType.SSO_LOGIN_FAIL);
		
		authService.checkSsoAuthCode(type, code);
		return ResponseEntity
                .status(SuccessType.LOGIN_SSO_SUCCESS.getStatus())
                .body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}

	@PostMapping("login/user")
	@Operation(
			summary = "SSO 로그인 요청",
			description = "카카오 또는 구글 액세스 토큰을 이용하여 사용자 로그인을 처리합니다.",
			tags = {"SSO Authentication"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "SSO 로그인 요청 데이터",
					required = true
			),
			responses = {
					@ApiResponse(responseCode = "200", description = "로그인 성공 (액세스 토큰 및 리프레시 토큰 발급)"),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류"),
					@ApiResponse(responseCode = "501", description = "현재 해당 소셜 로그인 방식은 아직 지원되지 않습니다.")
			}
	)
	public ResponseEntity<?> login(@RequestBody LoginRequest request){

		TokenDto tokenDto = ssoService.login(request);

		return ResponseEntity.ok()
				.header("Authorization",tokenDto.accessToken())
				.header("Set-Cookie",tokenDto.refreshToken())
				.body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}
}
