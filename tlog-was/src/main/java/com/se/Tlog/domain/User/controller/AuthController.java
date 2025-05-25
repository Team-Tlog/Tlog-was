package com.se.Tlog.domain.User.controller;

import com.se.Tlog.global.util.jwt.JwtUtil;
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

	private final AuthenticationService authenticationService;
	private final SsoAuthService ssoAuthService;
	private final JwtUtil jwtUtil;

	@GetMapping("/sso/login/kakao")
	public ResponseEntity<SuccessRes<SsoLoginRequest>> getSsoLoginByKakao() {
		return ResponseEntity.ok(
				SuccessRes.from(authenticationService.getSsoLoginRequest(SsoType.KAKAO)));
	}

	@GetMapping("/sso/login/google")
	public ResponseEntity<SuccessRes<SsoLoginRequest>> getSsoLoginByGoogle() {
		return ResponseEntity.ok(
				SuccessRes.from(authenticationService.getSsoLoginRequest(SsoType.GOOGLE)));
	}

	@GetMapping("/sso/callback/kakao")
	@Operation(hidden = true)
	public ResponseEntity<SuccessRes<?>> getSsoCallbackByKakao(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.KAKAO, code, error);
	}

	@GetMapping("/sso/callback/google")
	@Operation(hidden = true)
	public ResponseEntity<SuccessRes<?>> getSsoCallbackByGoogle(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.GOOGLE, code, error);
	}
	
	private ResponseEntity<SuccessRes<?>> ssoCallBack(SsoType type, String code, String error) {
		if (error != null)
			throw new CustomException(ErrorType.SSO_LOGIN_FAIL);

		authenticationService.checkSsoAuthCode(type, code);
		return ResponseEntity
                .status(SuccessType.LOGIN_SSO_SUCCESS.getStatus())
                .body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}

	@PostMapping("login/user")
	@Operation(
			summary = "SSO 로그인 요청",
			description = "카카오 또는 구글 액세스 토큰을 이용하여 사용자 로그인을 처리합니다."
			            + "<br/>카카오는 액세스 토큰, 구글은 ID 토큰을 사용합니다.",
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

		TokenDto tokenDto = ssoAuthService.login(request);

		return ResponseEntity.ok()
				.header("Authorization",tokenDto.accessToken())
				.header("Set-Cookie",tokenDto.refreshToken())
				.body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}

	@PostMapping("/logout")
	@Operation(
			summary = "SSO 로그아웃 요청",
			description = "사용자 accessToken 과 refreshToken을 쿠키로 받아 처리합니다.",
			tags = {"SSO Authentication"},
			responses = {
					@ApiResponse(responseCode = "200", description = "로그아웃 성공 (토큰 블랙리스트 처리 완료)"),
					@ApiResponse(responseCode = "500", description = "블랙리스트 등록 실패")
			}
	)
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader,
									@CookieValue(value = "refreshToken", required = false)String refreshToken) {
		String token = jwtUtil.resolveToken(authorizationHeader);
		System.out.println("refreshToken = " + refreshToken);

		ssoAuthService.logout(token,refreshToken);
		return ResponseEntity.ok()
					.body(SuccessRes.from(SuccessType.LOGOUT_SUCCESS));
	}
}
