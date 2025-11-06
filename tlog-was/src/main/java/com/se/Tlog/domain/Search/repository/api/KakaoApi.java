package com.se.Tlog.domain.Search.repository.api;

import com.se.Tlog.domain.Search.repository.dto.KakaoLocalSearchRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoApi {
    private final String KAKAO_LOCAL_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category";

    @Value("${sso.clientId.kakao}")
    private String KAKAO_CLIENT_ID; // 서비스 사용을 위한 애플리케이션 등록 id

    private final String CATEGORY_EATERY = "FD6";
    private final String CATEGORY_CAFE = "CE7";

    public KakaoLocalSearchRes searchEatery(double latitude, double longitude) {
        return searchPlaceByKakao(latitude, longitude, CATEGORY_EATERY, 1, 10);
    }

    public KakaoLocalSearchRes searchCafe(double latitude, double longitude) {
        return searchPlaceByKakao(latitude, longitude, CATEGORY_CAFE, 1, 10);
    }

    private KakaoLocalSearchRes searchPlaceByKakao(double latitude, double longitude, String categoryType, int page, int size) {
        return RestClient.create(KAKAO_LOCAL_SEARCH_URL).get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("category_group_code", categoryType)
                        .queryParam("x", longitude)
                        .queryParam("y", latitude)
                        .queryParam("radius", 5000) // 5km로 고정 (최대 20000까지)
                        .queryParam("page", page) // 1~45, 기본값 1
                        .queryParam("size", size) // 1~15, 기본값 15
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_CLIENT_ID)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("음식점 조회 API 호출 중 에러가 발생했습니다.");
                    log.error("request : " + req.getURI().toString() + req.getHeaders().toString());
                    log.error("response : " + new String(res.getBody().readAllBytes()));
                    throw new CustomException(ErrorType.RESTAURANT_FETCH_FAIL);
                })
                .body(KakaoLocalSearchRes.class);
    }
}
