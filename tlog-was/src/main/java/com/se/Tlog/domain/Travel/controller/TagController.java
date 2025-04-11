package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.application.TagService;
import com.se.Tlog.domain.Travel.controller.dto.TagDto;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    @Operation(
            summary = "새로운 여행지 고유 태그 생성",
            description = "새로운 여행지 고유 태그를 생성합니다.",
            tags = {"고유 Tag 관리"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            responses = {
                    @ApiResponse(responseCode = "201", description = "새로운 태그 등록을 성공하였습니다."),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 태그입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> createTag(@RequestBody TagDto tagDto) {
        tagService.createTag(tagDto);
        return ResponseEntity.ok().body(SuccessRes.from(SuccessType.TAG_CREATED));
    }

    @GetMapping
    @Operation(
            summary = "활성화된 태그 리스트 반환",
            description = "활성화된 태그들을 리스트 형태로 반환합니다.",
            tags = {"고유 Tag 관리"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공했습니다."),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 태그입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> getActiveAllTags(@PageableDefault(size = 10)Pageable pageable) {
        return ResponseEntity.ok().body(SuccessRes.from(tagService.getAllActiveTags(pageable)));
    }

    @PutMapping("{tagId}")
    @Operation(
            summary = "태그 삭제 상태 수정",
            description = "특정 태그의 삭제 여부를 수정합니다. (isDeleted 값을 true 또는 false로 설정)",
            tags = {"고유 Tag 관리"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {
                    @Parameter(name = "tagId", description = "삭제할 태그의 UUID 입니다."),
                    @Parameter(name = "isDeleted", description = "true: 삭제 상태로 변경, false: 복구 상태로 변경")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공했습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 태그 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> updateTagDeletedStatus(@PathVariable("tagId") String tagId,
                                                    @RequestParam boolean isDeleted){
        tagService.updateTagDeletedStatus(tagId,isDeleted);
        return ResponseEntity.ok().body(SuccessRes.from(SuccessType.TAG_UPDATE));
    }
}
