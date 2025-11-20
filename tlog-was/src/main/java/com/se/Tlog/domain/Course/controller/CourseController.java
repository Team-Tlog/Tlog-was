package com.se.Tlog.domain.Course.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.se.Tlog.domain.Course.application.CourseOrchestrationService;
import com.se.Tlog.domain.Course.controller.dto.*;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.se.Tlog.domain.Course.domain.OwnerType;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Tag(name = "여행 코스")
@SecurityRequirement(
        name = "JwtAuthScheme",
        scopes = {"scope1", "scope2"})
public class CourseController {
    private final CourseOrchestrationService courseOrchestrationService;


    @PostMapping("/recommendations") // GET으로 변경
    @Operation(
            summary = "AI 코스 추천 미리보기",
            description = "사용자의 요청 조건(쿼리 파라미터)에 따라 AI 추천 여행지와 Wishlist를 합쳐 지역별로 그룹화된 목록을 반환합니다.",
            parameters = {
                    @Parameter(name = "ownerId", description = "소유자 ID (User 또는 Team)", required = true, example = "8a0e05ee-49f3-4d7a-8f83-e023190d655f"),
                    @Parameter(name = "ownerType", description = "소유자 타입 (USER 또는 TEAM)", required = true, example = "USER")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 지역별 그룹화된 추천 목록을 반환합니다.",
                            content = @Content(schema = @Schema(implementation = SuccessRes.class))),
                    @ApiResponse(responseCode = "404", description = "팀 리더를 찾을 수 없거나 AI 추천에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))})
    public ResponseEntity<?> getRecommendations(
            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("ownerType") OwnerType ownerType,
            @Valid @RequestBody CourseRecommendationReq request) {

        Map<String, List<RecommendedDestinationDto>> recommendations =
                courseOrchestrationService.getCourseRecommendations(ownerId, ownerType, request);

        return ResponseEntity.
                status(SuccessType.OK.getStatus())
                .body(SuccessRes.from(recommendations));
    }

    @PostMapping
    @Operation(
            summary = "최종 코스 저장",
            description = "사용자가 편집을 완료한 코스 정보를 받아 DB에 저장하고, 생성된 코스 ID를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "코스 생성 성공. 생성된 코스 ID를 반환합니다.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 저장에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))})
    public ResponseEntity<?> saveCourse(
            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("ownerType") OwnerType ownerType,
            @Valid @RequestBody CourseSaveReq request) {

        String courseId = courseOrchestrationService.saveCourse(ownerId, ownerType, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessRes.from(courseId));
    }

    /*@GetMapping("/{courseId}")
    @Operation(
            summary = "코스 상세 조회",
            description = "저장된 특정 코스 ID에 해당하는 상세 정보를 조회하고 지역별로 그룹화하여 반환합니다.",
            parameters = { @Parameter(name = "courseId", description = "조회할 코스 ID", required = true, example = "uuid-string-123") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 코스 상세 정보(지역별 그룹화)를 반환합니다.",
                            content = @Content(schema = @Schema(implementation = SuccessRes.class))),
                    @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))})
    public ResponseEntity<?> getCourseDetail(
            @PathVariable("courseId") String courseId) {

        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .body(SuccessRes.from(courseOrchestrationService.getCourseDetail(courseId)));
    }*/

    @GetMapping("/closest") // 엔드포인트 변경
    @Operation(
            summary = "현재 시점과 가장 가까운 코스 상세 조회",
            description = "ownerId를 기반으로 현재 날짜와 시작일이 가장 가까운 코스를 찾아 상세 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "ownerId", description = "소유자 ID (User 또는 Team)", required = true, example = "8a0e05ee-49f3-4d7a-8f83-e023190d655f"),
                    @Parameter(name = "ownerType", description = "소유자 타입 (USER 또는 TEAM)", required = true, example = "USER")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 코스 상세 정보(지역별 그룹화)를 반환합니다.",
                            content = @Content(schema = @Schema(implementation = SuccessRes.class))),
                    @ApiResponse(responseCode = "404", description = "해당 소유자의 코스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))})
    public ResponseEntity<?> getClosestCourseDetail(
            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("ownerType") OwnerType ownerType) {

        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .body(SuccessRes.from(courseOrchestrationService.getClosestCourseDetail(ownerId, ownerType)));
    }
}
