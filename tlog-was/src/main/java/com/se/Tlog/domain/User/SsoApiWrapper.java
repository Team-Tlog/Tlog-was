package com.se.Tlog.domain.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@Component
public class SsoApiWrapper {
	// 각 SSO Auth 제공 서버별 인증 요청 API 규격입니다.
	// (GET 프로토콜)
	private final String KAKAO_REQUEST_API = "https://kauth.kakao.com/oauth/authorize";
	private final String GOOGLE_REQUEST_API = "https://accounts.google.com/o/oauth2/v2/auth";

	// 각 SSO Auth 제공 서버별 AccessToken 요청 API 규격입니다.
	// (POST 프로토콜)
	private final String KAKAO_AUTHORIZATION_API = "https://kauth.kakao.com/oauth/token";
	private final String GOOGLE_AUTHORIZATION_API = "https://oauth2.googleapis.com/token";
	
	// Auth 서비스 연결을 위한 애플리케이션 등록 id 
	@Value("${sso.clientId.kakao}")
	private String KAKAO_CLIENT_ID;
	@Value("${sso.clientId.google}")
	private String GOOGLE_CLIENT_ID;

	// Auth 서비스 연결을 위한 애플리케이션 등록 secret key
	@Value("${sso.clientSecret.google}")
	private String GOOGLE_CLIENT_SECRET;
	
	// 로그인 요청시 callback(redirect)받을 url
	@Value("${sso.callback.kakao}")
	private String KAKAO_CALLBACK;
	@Value("${sso.callback.google}")
	private String GOOGLE_CALLBACK;
	
	
	public String getAuthLoginUrl(SsoType type) {
		if (type == SsoType.KAKAO) {
			return getKakaoLoginUrl();
		}
		if (type == SsoType.GOOGLE) {
			return getGoogleLoginUrl();
		}
		
		throw new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN);
	}
	
	/**
	 * 지정된 SSO Auth 제공 서버로부터 accessToken 획득을 요청합니다.
	 * @param type
	 * @param code
	 * @return
	 * @exception CustomException 액세스 토큰 취득에 실패할 경우 발생.
	 */
	public String getAccessToken(SsoType type, String code) {
		if (type == SsoType.KAKAO) {
			return getKakaoAccessToken(code);
		}
		if (type == SsoType.GOOGLE) {
			return getGoogleAccessToken(code);
		}
		
		throw new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN);
	}
	
	private String getKakaoLoginUrl() {
		return KAKAO_REQUEST_API +
				"?client_id=" + KAKAO_CLIENT_ID
				+ "&redirect_uri=" + KAKAO_CALLBACK
				+ "&response_type=code";
	}

	private String getGoogleLoginUrl() {
		return GOOGLE_REQUEST_API +
				"?client_id=" + GOOGLE_CLIENT_ID
				+ "&redirect_uri=" + GOOGLE_CALLBACK
				+ "&scope=" + "email"
				+ "&response_type=code";
	}
	
	private String getKakaoAccessToken(String code) {
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
	
	private String getGoogleAccessToken(String code) {
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
}
