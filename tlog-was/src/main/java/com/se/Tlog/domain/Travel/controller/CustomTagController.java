package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.controller.dto.AddCustomTagsReq;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/custom-tags")
@RequiredArgsConstructor
public class CustomTagController {
    private final CustomTagService customTagService;


    @PostMapping("/{destinationId}")
    @Operation(
            summary = "특정 여행지에 커스텀 태그를 추가합니다.",
            description = "특정 id 여행지의 커스텀 태그를 생성합니다.",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {@Parameter(name = "destinationId", description = "특정 destinaion의 UUID 입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "400", description = "존재하지 않는 여행지입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> addCustomTag(@RequestBody AddCustomTagsReq addCustomTagsReq,
            @Parameter(description = "특정 destination의 UUID 입니다.") @PathVariable String destinationId) {
        customTagService.addCustomTag(destinationId,addCustomTagsReq.tagNameList());
        return ResponseEntity
                .status(SuccessType.CUSTOM_TAG_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.CUSTOM_TAG_CREATED));
    }
}
