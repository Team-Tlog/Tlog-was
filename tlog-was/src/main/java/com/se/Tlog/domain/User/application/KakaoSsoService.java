package com.se.Tlog.domain.User.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.presentation.dto.SsoUserInfo;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@ApplicationService
public class KakaoSsoService implements SsoService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String KAKAO_USER_INFO_API = "https://kapi.kakao.com/v2/user/me";

    @Override
    public SsoUserInfo getUserInfo(String accessToken) {
            try{
                System.out.println("🔹 [DEBUG] Access Token: " + accessToken);

                ResponseEntity<String> response = RestClient.create().get()
                        .uri(KAKAO_USER_INFO_API)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                        .retrieve()
                        .toEntity(String.class);

                System.out.println("🔹 [DEBUG] Kakao API Response: " + response.getBody());

                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                // 전화번호 정보 가져오기 (사용자가 동의했을 경우에만 존재)
                // String phoneNumber = jsonNode.path("kakao_account").path("phone_number").asText(null);

                return SsoUserInfo.from(jsonNode,"kakao");
            } catch (RestClientResponseException e){
                System.err.println("❌ [ERROR] Kakao API 호출 실패: " + e.getStatusCode() + " / " + e.getResponseBodyAsString());
                throw new CustomException(ErrorType.KAKAO_AUTH_FAIL);
            } catch (Exception e) {
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public SsoType getType() {
        return SsoType.KAKAO;
    }
}
