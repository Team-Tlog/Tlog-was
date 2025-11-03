package com.se.Tlog.domain.Guide.controller;

import com.se.Tlog.domain.Guide.controller.dto.GuideDto;
import com.se.Tlog.domain.Guide.service.GuideService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/local-guide")
@RequiredArgsConstructor
@Tag(name = "메인페이지 - 지역 행사 관련")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class GuideController {
    private final GuideService guideService;

    @GetMapping
    @Operation(
            summary = "지역 행사 조회",
            description = "사용자의 위치를 기반으로 지역 행사를 조회합니다."
                        + "<br>위치가 주어지지 않거나 잘못 입력되면 근처 행사는 조회되지 않습니다.",
            parameters = {
                    @Parameter(name = "latitude", description = "사용자의 위치정보 (위도, -90~90)"),
                    @Parameter(name = "longitude", description = "사용자의 위치정보 (경도, -180~180)")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 사용자의 코스 리스트를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Page<GuideDto>>> getLocalGuide(
            @RequestParam(name = "latitude", defaultValue = "-1000") Double latitude,
            @RequestParam(name = "longitude", defaultValue = "-1000") Double longitude,
            Pageable pageable) {
        return ResponseEntity.ok(SuccessRes.from(
                guideService.getLocalGuides(latitude, longitude, pageable)));
    }
}
