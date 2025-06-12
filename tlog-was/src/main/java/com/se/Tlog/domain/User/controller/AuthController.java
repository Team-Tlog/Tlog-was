package com.se.Tlog.domain.User.controller;

import com.se.Tlog.global.util.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.se.Tlog.domain.User.application.AuthenticationService;
import com.se.Tlog.domain.User.application.SsoAuthService;
import com.se.Tlog.domain.User.controller.dto.LoginRequest;
import com.se.Tlog.domain.User.controller.dto.RegisterRequest;
import com.se.Tlog.domain.User.controller.dto.SsoLoginRequest;
import com.se.Tlog.domain.User.controller.dto.TokenDto;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import lombok.RequiredArgsConstructor;

import java.util.Map;

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

    @PostMapping("/register/user")
    @Operation(
            summary = "SSO 회원가입 요청 (성공시 즉시 로그인합니다)",
            description = "카카오, 구글, 네이버 액세스 토큰을 이용하여 사용자 회원가입을 처리합니다."
                        + "<ul>"
                        + "  <li><b>토큰 사용 방침</b> : </li>"
                        + "  <ul>"
                        + "    <li>카카오 : 액세스 토큰</li>"
                        + "    <li>구글  : ID 토큰</li>"
                        + "    <li>네이버 : 액세스 토큰</li>"
                        + "  </ul>"
                        + "</ul>"
                        + "<br/>"
                        + "<br/><b>로그인 성공시 즉시 로그인합니다.</b>",
            tags = {"SSO Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SSO 회원가입 요청 데이터",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공 (액세스 토큰 및 리프레시 토큰 발급)"),
                    @ApiResponse(responseCode = "400", description = "회원가입에 요구되는 정보가 잘못되었거나 부족합니다."),
                    @ApiResponse(responseCode = "401", description = "SSO 인증 토큰이 유효하지 않습니다."),
                    @ApiResponse(responseCode = "409", description = "이미 회원가입된 사용자입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류"),
                    @ApiResponse(responseCode = "501", description = "현재 해당 소셜 로그인 방식은 아직 지원되지 않습니다.")
            }
    )
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){

        TokenDto tokenDto = ssoAuthService.register(request);

        return ResponseEntity.ok()
                .header("Authorization",tokenDto.accessToken())
                .header("Set-Cookie",tokenDto.refreshToken())
                .body(SuccessRes.of(SuccessType.LOGIN_SSO_SUCCESS, Map.of(
                        "firebaseCustomToken", tokenDto.firebaseCustomToken()
                )));
    }

	@PostMapping("/login/user")
	@Operation(
			summary = "SSO 로그인 요청",
			description = "카카오, 구글, 네이버 액세스 토큰을 이용하여 사용자 회원가입을 처리합니다."
                        + "<ul>"
                        + "  <li><b>토큰 사용 방침</b> : </li>"
                        + "  <ul>"
                        + "    <li>카카오 : 액세스 토큰</li>"
                        + "    <li>구글  : ID 토큰</li>"
                        + "    <li>네이버 : 액세스 토큰</li>"
                        + "  </ul>"
                        + "</ul>",
			tags = {"SSO Authentication"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "SSO 로그인 요청 데이터",
					required = true
			),
			responses = {
					@ApiResponse(responseCode = "200", description = "로그인 성공 (액세스 토큰 및 리프레시 토큰 발급)"),
					@ApiResponse(responseCode = "401", description = "SSO 인증 토큰이 유효하지 않습니다."),
					@ApiResponse(responseCode = "404", description = "아직 회원가입하지 않은 사용자입니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류"),
					@ApiResponse(responseCode = "501", description = "현재 해당 소셜 로그인 방식은 아직 지원되지 않습니다.")
			}
	)
	public ResponseEntity<?> login(@RequestBody LoginRequest request){

		TokenDto tokenDto = ssoAuthService.login(request);

		return ResponseEntity.ok()
				.header("Authorization",tokenDto.accessToken())
				.header("Set-Cookie",tokenDto.refreshToken())
				.body(SuccessRes.of(SuccessType.LOGIN_SSO_SUCCESS, Map.of(
						"firebaseCustomToken", tokenDto.firebaseCustomToken()
				)));
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
