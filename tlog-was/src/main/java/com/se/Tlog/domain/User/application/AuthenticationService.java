package com.se.Tlog.domain.User.application;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.infrastructure.api.SsoOauthManager;
import com.se.Tlog.domain.User.presentation.dto.SsoLoginRequest;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class AuthenticationService {
	@Autowired
	private final Map<SsoType, SsoOauthManager> ssoOauthManagers;
	
	/**
	 * 사용자에게 외부 소셜 로그인을 요청하는 데이터를 반환합니다.
	 * @param type
	 * @return
	 */
	public SsoLoginRequest getSsoLoginRequest(SsoType type) {
		SsoOauthManager ssoOauthManager = Optional.ofNullable(ssoOauthManagers.get(type))
				.orElseThrow(() -> new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN));
		return new SsoLoginRequest(ssoOauthManager.getLoginUrl());
	}
	
	/**
	 * 사용자 응답 후 소셜 로그인 결과를 처리합니다.
	 * @param type
	 * @param code
	 * @exception CustomException 로그인 처리가 실패할 경우 발생.
	 */
	public void checkSsoAuthCode(SsoType type, String code) {
		SsoOauthManager ssoOauthManager = Optional.ofNullable(ssoOauthManagers.get(type))
				.orElseThrow(() -> new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN));
		String token = ssoOauthManager.getAccessToken(code);
		// 토큰 확인시 로그인, 회원가입 등등 처리
	}
}
