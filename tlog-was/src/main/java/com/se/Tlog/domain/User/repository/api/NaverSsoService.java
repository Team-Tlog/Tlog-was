package com.se.Tlog.domain.User.repository.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class NaverSsoService implements SsoService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String NAVER_USER_INFO_API = "https://openapi.naver.com/v1/nid/me";

    @Override
    public SsoUserInfo getUserInfo(String accessToken) {
            try{
                System.out.println("🔹 [DEBUG] Access Token: " + accessToken);

                ResponseEntity<String> response = RestClient.create().get()
                        .uri(NAVER_USER_INFO_API)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                        .retrieve()
                        .toEntity(String.class);

                System.out.println("🔹 [DEBUG] Naver API Response: " + response.getBody());

                JsonNode rootResponseNode = objectMapper.readTree(response.getBody());
                JsonNode profileNode = rootResponseNode.at("/response");
                
                // 기본 정보 외 기타 정보는 별도의 설정이 필요합니다.
                //    자세한 사항은 공식문서 참고 (Naver Developers)
                //    https://developers.naver.com/docs/login/profile/profile.md
                return new SsoUserInfo(
                        profileNode.path("id").asText(),
                        profileNode.path("email").asText(""), // 이메일
                        // profileNode.path("phone_number").asText(""), // 전화번호
                        profileNode.path("name").asText(""),
                        "naver");
            } catch (RestClientResponseException e){
                System.err.println("❌ [ERROR] Naver API 호출 실패: " + e.getStatusCode() + " / " + e.getResponseBodyAsString());
                throw new CustomException(ErrorType.NAVER_AUTH_FAIL);
            } catch (Exception e) {
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public SsoType getType() {
        return SsoType.NAVER;
    }
}
