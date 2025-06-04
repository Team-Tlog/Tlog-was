package com.se.Tlog.domain.Social.Follow.controller;

import com.se.Tlog.domain.Social.Follow.application.FollowService;
import com.se.Tlog.domain.Social.Follow.controller.dto.FollowDto;
import com.se.Tlog.domain.Social.Follow.controller.dto.FollowStatusDto;
import com.se.Tlog.domain.User.controller.dto.UserSummaryDto;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Tag(name = "Follow")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class FollowController {
    private final FollowService followService;


    @PostMapping
    @Operation(
            summary = "사용자 팔로잉 걸기/취소",
            description = "특정 유저가 다른 유저를 팔로우합니다."
                    + "<br> 이미 팔로우되어있다면 팔로잉을 취소합니다."
                    + "<br> <b>갱신된 팔로우 상태가 반환됩니다.</b>",
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "404", description = "존재하지 않는 사용자 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<FollowStatusDto>> follow(@RequestBody FollowDto followDto) {
        FollowStatusDto followStatusDto = followService.follow(followDto);
        return ResponseEntity.ok()
                .body(SuccessRes.from(followStatusDto));
    }


    // 내가 팔로우한 사람들
    @GetMapping("/following/{userId}")
    @Operation(
            summary = "내가 팔로잉한 사람들 조회",
            description = "내가 팔로잉하고 있는 사람들을 모두 조회합니다.",
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "404", description = "존재하지 않는 사용자 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<Page<UserSummaryDto>>> getFollowingList(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(SuccessRes.from(followService.getFollowingList(userId,pageable)));
    }

    // 나를 팔로우하는 사람들
    @GetMapping("/followers/{userId}")
    @Operation(
            summary = "나를 팔로우하는 사람들 조회",
            description = "나를 팔로우하고 있는 사람들을 모두 조회합니다.",
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "404", description = "존재하지 않는 사용자 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<Page<UserSummaryDto>>> getFollowerList(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(SuccessRes.from(followService.getFollowerList(userId,pageable)));
    }

}
