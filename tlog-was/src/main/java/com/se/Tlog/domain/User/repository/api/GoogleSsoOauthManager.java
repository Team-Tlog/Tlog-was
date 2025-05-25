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
public class GoogleSsoOauthManager implements SsoOauthManager{
	// 인증 요청 API 규격입니다. (GET)
	private static final String GOOGLE_REQUEST_API = "https://accounts.google.com/o/oauth2/v2/auth";

	// AccessToken 요청 API 규격입니다. (POST)
	private static final String GOOGLE_AUTHORIZATION_API = "https://oauth2.googleapis.com/token";
	
	// Auth 서비스 연결을 위한 애플리케이션 등록 id 
	@Value("${sso.clientId.google}")
	private String GOOGLE_CLIENT_ID;

	// Auth 서비스 연결을 위한 애플리케이션 등록 secret key
	@Value("${sso.clientSecret.google}")
	private String GOOGLE_CLIENT_SECRET;
	
	// 로그인 요청시 callback(redirect)받을 url
	@Value("${sso.callback.google}")
	private String GOOGLE_CALLBACK;

	@Override
	public String getLoginUrl() {
		return GOOGLE_REQUEST_API +
				"?client_id=" + GOOGLE_CLIENT_ID
				+ "&redirect_uri=" + GOOGLE_CALLBACK
				+ "&scope=" + "email%20profile"
				+ "&response_type=code";
	}

	@Override
	public String getAccessToken(String code) {
		try {
			return RestClient.create().post()
					.uri(GOOGLE_AUTHORIZATION_API)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(
							"grant_type=authorization_code"
							+ "&client_id=" + GOOGLE_CLIENT_ID
							+ "&client_secret=" + GOOGLE_CLIENT_SECRET
							+ "&code=" + code
							+ "&redirect_uri=" + GOOGLE_CALLBACK)
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
		return SsoType.GOOGLE;
	}
}
