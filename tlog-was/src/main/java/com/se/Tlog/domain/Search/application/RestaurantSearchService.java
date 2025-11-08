package com.se.Tlog.domain.Search.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.controller.dto.RestaurantResultDto;
import com.se.Tlog.domain.Search.repository.api.KakaoApi;
import com.se.Tlog.domain.Search.repository.api.NaverApi;
import com.se.Tlog.domain.Search.repository.dto.*;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

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

    public List<RestaurantResultDto> searchEatery(double latitude, double longitude) {
        KakaoLocalSearchRes rawRes = kakaoApi.searchEatery(latitude, longitude);
        return Arrays.stream(rawRes.getDocuments())
                .map(this::makeResultByRestaurant)
                .toList();
    }

    public List<RestaurantResultDto> searchCafe(double latitude, double longitude) {
        KakaoLocalSearchRes results = kakaoApi.searchCafe(latitude, longitude);
        return Arrays.stream(results.getDocuments())
                .map(this::makeResultByRestaurant)
                .toList();
    }
}
