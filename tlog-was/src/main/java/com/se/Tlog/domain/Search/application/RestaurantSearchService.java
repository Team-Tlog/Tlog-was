package com.se.Tlog.domain.Search.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.controller.dto.RestaurantResultDto;
import com.se.Tlog.domain.Search.repository.api.KakaoApi;
import com.se.Tlog.domain.Search.repository.api.NaverApi;
import com.se.Tlog.domain.Search.repository.dto.*;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@ApplicationService
@RequiredArgsConstructor
public class RestaurantSearchService {
    private final KakaoApi kakaoApi;
    private final NaverApi naverApi;

    private RestaurantResultDto makeResultByRestaurant(KakaoLocalSearchRes.Document restaurant) {
        NaverSearchRes<NaverBlog> blogs = naverApi.searchBlog(restaurant.getPlace_name());
        NaverSearchRes<NaverImage> images = naverApi.searchImage(restaurant.getPlace_name());
        return RestaurantResultDto.from(restaurant, images.getItems(), blogs.getItems());
    }

    public RestaurantResultDto searchEatery(double latitude, double longitude) {
        KakaoLocalSearchRes rawRes = kakaoApi.searchEatery(latitude, longitude);
        int idx = new Random().nextInt(0, Math.min(5, rawRes.getDocuments().length));
        return makeResultByRestaurant(
                rawRes.getDocuments()[idx]);
    }

    public RestaurantResultDto searchCafe(double latitude, double longitude) {
        KakaoLocalSearchRes results = kakaoApi.searchCafe(latitude, longitude);
        int idx = new Random().nextInt(0, Math.min(5, results.getDocuments().length));
        return makeResultByRestaurant(
                results.getDocuments()[idx]);
    }
}
