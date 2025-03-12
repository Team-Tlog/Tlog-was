package com.se.Tlog.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SsoLoginRequest {
	/**
	 * 사용자에게 외부 소셜 로그인을 요청할 url입니다.
	 */
	private String ssoLoginUrl;
}
