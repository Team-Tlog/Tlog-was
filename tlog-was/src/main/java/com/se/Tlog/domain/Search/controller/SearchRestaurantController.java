package com.se.Tlog.domain.Search.controller;

import com.se.Tlog.domain.Search.application.RestaurantSearchService;
import com.se.Tlog.domain.Search.controller.dto.RestaurantResultDto;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "음식점 추천")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class SearchRestaurantController {
    private final RestaurantSearchService restaurantSearchService;

    @GetMapping("/eatery")
    @Operation(
            summary = "근처 음식점 추천",
            description = "반경 5km 이내의 음식점을 추천합니다.",
            parameters = {
                    @Parameter(name = "longitude", description = "기준 위치 경도"),
                    @Parameter(name = "latitude", description = "기준 위치 위도")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<RestaurantResultDto>> searchEatery(
            @RequestParam double longitude,
            @RequestParam double latitude) {
        return ResponseEntity.ok(SuccessRes.from(
                restaurantSearchService.searchEatery(latitude, longitude)));
    }

    @GetMapping("/cafe")
    @Operation(
            summary = "근처 카페 추천",
            description = "반경 5km 이내의 카페를 추천합니다.",
            parameters = {
                    @Parameter(name = "longitude", description = "기준 위치 경도"),
                    @Parameter(name = "latitude", description = "기준 위치 위도")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<RestaurantResultDto>> searchCafe(
            @RequestParam double longitude,
            @RequestParam double latitude) {
        return ResponseEntity.ok(SuccessRes.from(
                restaurantSearchService.searchCafe(latitude, longitude)));
    }
}
