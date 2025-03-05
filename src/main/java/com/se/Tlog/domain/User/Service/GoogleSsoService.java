package com.se.Tlog.domain.User.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.Tlog.domain.User.dto.SsoUserInfo;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GoogleSsoService implements SsoService{
    private final String GOOGLE_USER_INFO_API = "https://www.googleapis.com/oauth2/v3/userinfo";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SsoUserInfo getUserInfo(String accessToken) {
        try {
            ResponseEntity<String> response = RestClient.create().get()
                    .uri(GOOGLE_USER_INFO_API)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return SsoUserInfo.from(jsonNode, "google");
        } catch (RestClientResponseException e) {
            throw new CustomException(ErrorType.GOOGLE_AUTH_FAIL);
        } catch (Exception e) {
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }
}
