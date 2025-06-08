package com.se.Tlog.domain.Social.Post.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Social.Post.application.PostService;
import com.se.Tlog.domain.Social.Post.controller.dto.CreatePostReq;
import com.se.Tlog.domain.Social.Post.controller.dto.PostDetailRes;
import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;
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

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "SNS - 코스 리뷰(게시물)")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class PostController {
    private final PostService postService;
    
    @PostMapping("/post")
    @Operation (
            summary = "코스 리뷰(게시물) 작성",
            description = "특정 사용자가 코스 리뷰(게시물)를 작성합니다.<br>등록에 성공한 코스 리뷰(게시글)의 상세 데이터를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<PostDetailRes>> createPostOfUser(@RequestBody CreatePostReq request) {
        String newPostId = postService.createPost(request);
        return ResponseEntity.ok(SuccessRes.from(
                postService.getPostDetail(newPostId)));
    }
    
    @GetMapping("/user/{userId}/posts/preview")
    @Operation (
            summary = "사용자의 코스 리뷰(게시물) 미리보기 정보",
            description = "특정 사용자의 코스 리뷰(게시물)들을 미리보기 형태로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Page<PostPreviewRes>>> getPreviewPostOfUser(@PathVariable(name = "userId") UUID userId, Pageable pageable) {
        return ResponseEntity.ok(SuccessRes.from(
                postService.getPreviewUserPosts(userId, pageable)));
    }
    
    @GetMapping("/post/{postId}")
    @Operation (
            summary = "코스 리뷰(게시물) 상세보기 정보",
            description = "특정 코스 리뷰(게시물)의 상세보기 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<PostDetailRes>> getDetailPost(@PathVariable(name = "postId") String postId) {
        return ResponseEntity.ok(SuccessRes.from(
                postService.getPostDetail(postId)));
    }
    
    @GetMapping("/posts")
    @Operation (
            summary = "팔로잉 사용자들의 최근 게시물",
            description = "현재 팔로우중인 사용자들의 게시물의 상세 정보를 조회합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            parameters = {
                    @Parameter(name = "lastPostId", description = "이 게시글 이후부터 조회합니다. 없으면 null 또는 0으로 기입합니다."),
                    @Parameter(name = "size", description = "추가 조회할 게시물의 갯수입니다. (paging과 유사)")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Slice<PostDetailRes>>> getRecentPost(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "lastPostId", required = false) String lastPostId,
            @RequestParam(name = "size") int size) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
            
        return ResponseEntity.ok(SuccessRes.from(
                postService.getRecentFollowersPosts(size, lastPostId, UUID.fromString(user.getId()))));
    }
}
