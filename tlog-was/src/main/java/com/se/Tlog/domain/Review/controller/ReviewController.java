package com.se.Tlog.domain.Review.controller;

import com.se.Tlog.domain.Review.application.ReviewService;
import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.controller.dto.ReviewCreateDto;

import com.se.Tlog.domain.Review.domain.SortType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(
            summary = "리뷰 생성",
            description = "새로운 리뷰를 생성합니다.",
            tags = {"Review 도메인"},
            security = @SecurityRequirement(name = "JwtAuthScheme"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "리뷰가 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "400", description = "입력값이 잘못되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<?>> createReview(@RequestBody ReviewCreateDto reviewCreateDto) {
        reviewService.createReview(reviewCreateDto);
        return ResponseEntity
                .status(SuccessType.REVIEW_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.REVIEW_CREATED));
    }

    @GetMapping("/{destinationId}")
    @Operation(
            summary = "여행지별 리뷰 조회",
            description = "특정 여행지에 대한 리뷰들을 페이지네이션하여 조회합니다.",
            tags = {"Review 도메인"},
            security = @SecurityRequirement(name = "JwtAuthScheme"),
            parameters = {
                    @Parameter(name = "destinationId", description = "리뷰를 조회할 여행지의 ID", required = true),
                    @Parameter(name = "sortType", description = "정렬 기준 (RECENT, HIGH_SCORE, LOW_SCORE)", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "404", description = "해당 여행지의 리뷰가 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<Slice<DestinationReviewDto>>> getReviewsByDestinationId(
            @PathVariable String destinationId,
            @RequestParam SortType sortType,
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(
                SuccessRes.from(reviewService.getReviewsByDestinationId(destinationId, sortType, pageable)));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(
            summary = "리뷰 삭제",
            description = "자신이 작성한 리뷰를 삭제합니다.",
            tags = {"Review 도메인"},
            security = @SecurityRequirement(name = "JwtAuthScheme"),
            parameters = {
                    @Parameter(name = "reviewId", description = "삭제할 리뷰의 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 삭제되었습니다."),
                    @ApiResponse(responseCode = "403", description = "자신의 리뷰가 아니어서 삭제할 수 없습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<?>> deleteReview(
            @PathVariable String reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getId();
        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity.ok().body(SuccessRes.from(SuccessType.OK));
    }
}