package com.se.Tlog.domain.User.repository.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Slf4j

@Service
public class GoogleSsoService implements SsoService{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SsoUserInfo getUserInfo(String accessToken) {
    	// Id Token을 사용하는 인증 방식입니다.
    	
    	GoogleIdToken idToken = Optional.ofNullable(getIdToken(accessToken))
    			.orElseThrow(() -> new CustomException(ErrorType.GOOGLE_AUTH_FAIL));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", idToken.getPayload().getSubject());
		map.put("email", idToken.getPayload().getEmail());
		map.put("nickname", idToken.getPayload().get("name"));
		
        JsonNode jsonNode = objectMapper.valueToTree(map);
        return SsoUserInfo.from(jsonNode, "google");
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
