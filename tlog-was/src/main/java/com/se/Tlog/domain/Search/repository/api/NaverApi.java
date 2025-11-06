package com.se.Tlog.domain.Search.repository.api;

import com.se.Tlog.domain.Search.repository.dto.NaverBlog;
import com.se.Tlog.domain.Search.repository.dto.NaverImage;
import com.se.Tlog.domain.Search.repository.dto.NaverSearchRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class NaverApi {
    private final String NAVER_BLOG_SEARCH_URL = "https://openapi.naver.com/v1/search/blog.json";
    private final String NAVER_IMAGE_SEARCH_URL = "https://openapi.naver.com/v1/search/image";

    @Value("${sso.clientId.naver}")
    private String NAVER_CLIENT_ID; // 서비스 사용을 위한 애플리케이션 등록 id
    @Value("${sso.clientSecret.naver}")
    private String NAVER_SECRET; // 서비스 사용을 위한 애플리케이션 secret

    public NaverSearchRes<NaverBlog> searchBlog(String query) {
        return RestClient.create(NAVER_BLOG_SEARCH_URL).get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .build())
                .header("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_SECRET)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("네이버 블로그 검색 API 호출 중 에러가 발생했습니다.");
                    log.error("request : " + req.getURI().toString() + req.getHeaders().toString());
                    log.error("response : " + new String(res.getBody().readAllBytes()));
                    throw new CustomException(ErrorType.RESTAURANT_FETCH_FAIL);
                })
                .body(new ParameterizedTypeReference<NaverSearchRes<NaverBlog>>() {});
    }

    public NaverSearchRes<NaverImage> searchImage(String query) {
        return RestClient.create(NAVER_IMAGE_SEARCH_URL).get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .build())
                .header("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_SECRET)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("네이버 이미지 검색 API 호출 중 에러가 발생했습니다.");
                    log.error("request : " + req.getURI().toString() + req.getHeaders().toString());
                    log.error("response : " + new String(res.getBody().readAllBytes()));
                    throw new CustomException(ErrorType.RESTAURANT_FETCH_FAIL);
                })
                .body(new ParameterizedTypeReference<NaverSearchRes<NaverImage>>() {});
    }
}
