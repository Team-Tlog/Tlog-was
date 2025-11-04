package com.se.Tlog.domain.Guide.controller;

import com.se.Tlog.domain.Guide.controller.dto.RawGuideDto;
import com.se.Tlog.domain.Guide.service.GuideService;
import com.se.Tlog.global.response.error.ErrorRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
// @Profile("dev") // 현재 활성화 기준이 Swagger 임을 감안.
@RequestMapping("/api/test/local-guide")
@RequiredArgsConstructor
@Tag(name = "메인페이지 - 지역 행사 관련")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class GuideDevController {
    private final GuideService guideService;

    @GetMapping
    @Operation(
            summary = "[개발환경 전용] 지역 행사 데이터 조회",
            description = "[개발환경 전용] 지역 행사 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<Page<RawGuideDto>> getRawGuideData(Pageable pageable) {
        return ResponseEntity.ok(guideService.getAllGuides(pageable));
    }

    @PostMapping
    @Operation(
            summary = "[개발환경 전용] 지역 행사 데이터 추가",
            description = "[개발환경 전용] 지역 행사 데이터를 추가합니다."
                        + "<br/>생성된 데이터가 반환됩니다."
                        + "<br/>입력된 id는 반영되지 않습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<RawGuideDto> createGuideData(@RequestBody RawGuideDto guideDto) {
        return ResponseEntity.ok(guideService.createGuide(guideDto));
    }

    @PutMapping
    @Operation(
            summary = "[개발환경 전용] 지역 행사 데이터 수정",
            description = "[개발환경 전용] 지역 행사 데이터를 수정합니다."
                        + "<br/>변경된 데이터가 반환됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<RawGuideDto> updateGuideData(@RequestBody RawGuideDto guideDto) {
        return ResponseEntity.ok(guideService.updateGuide(guideDto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "[개발환경 전용] 지역 행사 데이터 삭제",
            description = "[개발환경 전용] 지역 행사 데이터를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity deleteGuideData(@PathVariable int id) {
        guideService.deleteGuideById(id);
        return ResponseEntity.ok(null);
    }
}
