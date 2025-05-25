package com.se.Tlog.domain.User.repository.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Slf4j

@Service
public class GoogleSsoService implements SsoService{
    @Override
    public SsoUserInfo getUserInfo(String accessToken) {
    	// Id Token을 사용하는 인증 방식입니다. (일반 AccessToken과 다름)
    	
    	GoogleIdToken idToken = Optional.ofNullable(getIdToken(accessToken))
    			.orElseThrow(() -> new CustomException(ErrorType.GOOGLE_AUTH_FAIL));
		
    	// 구글의 ID 토큰 페이로드에 포함되는 항목과 이름에 관해서는 다음을 참고 :
    	//      https://developers.google.com/identity/openid-connect/openid-connect?hl=ko#an-id-tokens-payload
    	
    	// 이메일 및 이름을 조회하기 위해서는, 소셜로그인을 실행하는 측(프론트 등)에서 scope 범위를 할당해서 로그인해야 함!
        // 만약 프론트에서 scope가 제한된 ID 토큰을 보내주면, 서버에서 name을 조회할 수 있는 방법은 없음.
        //    공식 문서 참고 : https://developers.google.com/identity/openid-connect/openid-connect?hl=ko#scope-param
        return new SsoUserInfo(
                idToken.getPayload().getSubject(),
                idToken.getPayload().getEmail(), // 프론트에서 ID 토큰의 scope에 email을 포함해야 함.
                idToken.getPayload().get("name").toString(), // 프론트에서 ID 토큰의 scope에 profile을 포함해야 함.
                "google");
    }
    
    private GoogleIdToken getIdToken(String IdTokenString) {
    	try {
			return new GoogleIdTokenVerifier
					.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
	                .build()
	                .verify(IdTokenString);
        } catch (IllegalArgumentException e) {
			throw new CustomException(ErrorType.GOOGLE_AUTH_FAIL);
		} catch (Exception e) {
        	log.error("Google SSO verifying Error : ", e);
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SsoType getType() {
        return SsoType.GOOGLE;
    }
}
