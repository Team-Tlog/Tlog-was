package com.se.Tlog.domain.User.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.infrastructure.api.SsoApiWrapper;
import com.se.Tlog.domain.User.presentation.dto.SsoLoginRequest;
import com.se.Tlog.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	@Autowired
	private final SsoApiWrapper apiWrapper;
	
	/**
	 * 사용자에게 외부 소셜 로그인을 요청하는 데이터를 반환합니다.
	 * @param type
	 * @return
	 */
	public SsoLoginRequest getSsoLoginRequest(SsoType type) {
		return new SsoLoginRequest(apiWrapper.getAuthLoginUrl(type));
	}
	
	/**
	 * 사용자 응답 후 소셜 로그인 결과를 처리합니다.
	 * @param type
	 * @param code
	 * @exception CustomException 로그인 처리가 실패할 경우 발생.
	 */
	public void checkSsoAuthCode(SsoType type, String code) {
		String token = apiWrapper.getAccessToken(type, code);
		System.out.println("token = " + token);
		// 토큰 확인시 로그인, 회원가입 등등 처리
	}
}
