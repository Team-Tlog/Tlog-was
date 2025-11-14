package com.se.Tlog.domain.Guide.controller;

import com.se.Tlog.domain.Guide.controller.dto.BannerDestinationsDto;
import com.se.Tlog.domain.Guide.controller.dto.BannerDto;
import com.se.Tlog.domain.Guide.controller.dto.RecommendDestDto;
import com.se.Tlog.domain.Guide.controller.dto.RecommendPostDto;
import com.se.Tlog.domain.Guide.service.RecommendService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@Tag(name = "메인페이지 - 여행지, 게시글 추천")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/banners")
    @Operation(
            summary = "배너 조회 (인증 토큰 필요)",
            description = "메인페이지의 배너를 조회합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 배너 정보를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<BannerDto>>> getBanners(
            @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);

        return ResponseEntity.ok(SuccessRes.from(
                recommendService.getBanners(UUID.fromString(user.getId()))));
    }

    @GetMapping("/destinations/{bannerId}")
    @Operation(
            summary = "배너에서 추천된 여행지 조회 (인증 토큰 필요)",
            description = "배너에서 추천된 여행지를 조회합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 여행지 정보를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<BannerDestinationsDto>> getBannerDestinations(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String bannerId,
            @Parameter(example = "{\n  \"page\": 0,\n  \"size\": 10\n}")
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);

        return ResponseEntity.ok(SuccessRes.from(
                recommendService.getBannerDests(UUID.fromString(user.getId()), bannerId, pageable)));
    }

    @GetMapping("/destinations")
    @Operation(
            summary = "명소 추천 조회",
            description = "메인페이지의 명소 추천 결과를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 여행 명소 정보를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<RecommendDestDto>>> getDestinations() {
        return ResponseEntity.ok(SuccessRes.from(
                recommendService.getRecommendDests()));
    }

    @GetMapping("/posts")
    @Operation(
            summary = "추천 게시글 조회",
            description = "메인페이지의 추천 게시글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 추천 게시글 정보를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<RecommendPostDto>>> getPosts() {
        return ResponseEntity.ok(SuccessRes.from(
                recommendService.getRecommendPosts()));
    }
}
