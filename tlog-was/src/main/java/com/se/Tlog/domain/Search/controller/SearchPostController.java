package com.se.Tlog.domain.Search.controller;

import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Search.application.SnsPostSearchService;
import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;
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
@RequestMapping("/api/search/post")
@RequiredArgsConstructor
@Tag(name = "검색")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class SearchPostController {
    private final SnsPostSearchService postSearchService;
    
    @GetMapping("/by-destination-and-content")
    @Operation (
            summary = "게시물의 내용 및 코스 내 여행지 이름으로 검색",
            description = "게시물 내 내용이나 게시물이 참조하는 코스의 여행지 이름으로 게시물을 검색합니다.",
            parameters = {
                    @Parameter(name = "lastPostId", description = "이 게시글 이후부터 조회합니다. 없으면 null 또는 0으로 기입합니다."),
                    @Parameter(name = "size", description = "추가 조회할 게시물의 갯수입니다. (paging과 유사)"),
                    @Parameter(name = "query", description = "검색어입니다. (게시글 본문 / 여행지 이름이 검색됨) <b>최소 1글자 이상 입력해야 합니다!</b>")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Slice<PostPreviewRes>>> searchDestinationByName(
            @RequestParam(name = "lastPostId", required = false) String lastPostId,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "query") String query) {
        return ResponseEntity.ok(SuccessRes.from(
                postSearchService.searchOfDestinationAndContent(size, lastPostId, query)));
    }
}
