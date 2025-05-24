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

                JsonNode rootResponseNode = objectMapper.readTree(response.getBody());
                JsonNode profileNode = rootResponseNode.at("/kakao_account/profile");
                
                // 이메일 및 전화번호 등 기타 정보는 프론트 앱이 비즈 앱으로 등록되어야 조회할 수 있습니다.
                //    자세한 사항은 공식문서 및 카카오 동의항목 설정 페이지 참고 (Kakao Developers)
                //    https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
                //    https://developers.kakao.com/console/app/1197999/product/login/scope
                return new SsoUserInfo(
                        rootResponseNode.path("id").asText(),
                        "", // profileNode.path("email").asText(""), // 이메일
                            // profileNode.path("phone_number").asText(""), // 전화번호
                        profileNode.path("nickname").asText(""),
                        "kakao");
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
