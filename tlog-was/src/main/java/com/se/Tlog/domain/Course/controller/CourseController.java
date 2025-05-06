package com.se.Tlog.domain.Course.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Course.application.CourseService;
import com.se.Tlog.domain.Course.controller.dto.CreateCourseRequestDto;
import com.se.Tlog.domain.Course.domain.OwnerType;
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

@Controller
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Tag(name = "여행 코스")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class CourseController {
    private final CourseService courseService;
    
    @PostMapping("/user/{userId}")
    @Operation (
            summary = "유저 코스 생성",
            description = "사용자의 새로운 코스를 생성합니다.",
            parameters = { @Parameter(name = "userId", description = "코스를 생성할 사용자의 id") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 생성된 여행 코스 id를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 생성에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<String>> createCourseOfUser(
            @PathVariable(name = "userId") UUID userId,
            @RequestBody CreateCourseRequestDto request) {
        return ResponseEntity.ok(SuccessRes.from(
                courseService.createCourse(userId, OwnerType.USER, request)));
    }
    
    @PostMapping("/team/{teamId}")
    @Operation (
            summary = "팀 코스 생성",
            description = "팀에 새로운 코스를 생성합니다.",
            parameters = { @Parameter(name = "teamId", description = "코스를 생성할 팀의 id") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 생성된 여행 코스 id를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 생성에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<String>> createCourseOfTeam(
            @PathVariable(name = "teamId") UUID teamId,
            @RequestBody CreateCourseRequestDto request) {
        return ResponseEntity.ok(SuccessRes.from(
                courseService.createCourse(teamId, OwnerType.TEAM, request)));
    }
}
