package com.se.Tlog.domain.User.repository.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@Component
public class KakaoSsoOauthManager implements SsoOauthManager {
	// 인증 요청 API 규격입니다. (GET)
	private static final String KAKAO_REQUEST_API = "https://kauth.kakao.com/oauth/authorize";

	// AccessToken 요청 API 규격입니다. (POST)
	private static final String KAKAO_AUTHORIZATION_API = "https://kauth.kakao.com/oauth/token";
	
	// Auth 서비스 연결을 위한 애플리케이션 등록 id 
	@Value("${sso.clientId.kakao}")
	private String KAKAO_CLIENT_ID;
	
	// 로그인 요청시 callback(redirect)받을 url
	@Value("${sso.callback.kakao}")
	private String KAKAO_CALLBACK;
	
	@Override
	public String getLoginUrl() {
		return KAKAO_REQUEST_API +
				"?client_id=" + KAKAO_CLIENT_ID
				+ "&redirect_uri=" + KAKAO_CALLBACK
				+ "&response_type=code";
	}

	@Override
	public String getAccessToken(String code) {
		try {
			return RestClient.create().post()
					.uri(KAKAO_AUTHORIZATION_API)
					.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(
							"grant_type=authorization_code"
							+ "&client_id=" + KAKAO_CLIENT_ID
							+ "&redirect_uri=" + KAKAO_CALLBACK
							+ "&code=" + code)
					.retrieve()
					.toEntity(String.class)
					.getBody();
		}
		catch (RestClientResponseException e) {
			throw new CustomException(ErrorType.SSO_ACCESSTOKEN_FAIL);
		}
	}

	@Override
	public SsoType getType() {
		return SsoType.KAKAO;
	}

}
