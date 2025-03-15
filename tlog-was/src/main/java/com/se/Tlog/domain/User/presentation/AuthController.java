package com.se.Tlog.domain.User.presentation;

import com.se.Tlog.domain.User.presentation.dto.LoginRequest;
import com.se.Tlog.domain.User.presentation.dto.SsoLoginRequest;
import com.se.Tlog.domain.User.presentation.dto.TokenDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.se.Tlog.domain.User.application.AuthenticationService;
import com.se.Tlog.domain.User.application.SsoAuthService;
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
	public ResponseEntity<SsoLoginRequest> getSsoLoginByKakao() {
		return ResponseEntity.ok(authService.getSsoLoginRequest(SsoType.KAKAO));
	}

	@GetMapping("/sso/login/google")
	public ResponseEntity<SsoLoginRequest> getSsoLoginByGoogle() {
		return ResponseEntity.ok(authService.getSsoLoginRequest(SsoType.GOOGLE));
	}

	@GetMapping("/sso/callback/kakao")
	public ResponseEntity<?> getSsoCallbackByKakao(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.KAKAO, code, error);
	}

	@GetMapping("/sso/callback/google")
	public ResponseEntity<?> getSsoCallbackByGoogle(
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "error", required = false) String error) {
		return ssoCallBack(SsoType.GOOGLE, code, error);
	}
	
	private ResponseEntity<?> ssoCallBack(SsoType type, String code, String error) {
		if (error != null)
			throw new CustomException(ErrorType.SSO_LOGIN_FAIL);
		
		authService.checkSsoAuthCode(type, code);
		return ResponseEntity
                .status(SuccessType.LOGIN_SSO_SUCCESS.getStatus())
                .body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}

	@PostMapping("login/user")
	public ResponseEntity<?> login(@RequestBody LoginRequest request){

		TokenDto tokenDto = ssoService.login(request);

		return ResponseEntity.ok()
				.header("Authorization",tokenDto.accessToken())
				.header("Set-Cookie",tokenDto.refreshToken())
				.body(SuccessRes.from(SuccessType.LOGIN_SSO_SUCCESS));
	}
}
