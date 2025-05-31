package com.se.Tlog.domain.Tbti.controller;

import com.se.Tlog.domain.Tbti.application.TbtiService;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiQuestionResponse;
import com.se.Tlog.global.response.success.SuccessRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tbti")
@RequiredArgsConstructor
@Tag(name = "TBTI 도메인")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TbtiController {
    private final TbtiService tbtiService;

    @GetMapping("/user/questions")
    @Operation (
    		summary = "사용자용 TBTI 질문 전체 가져오기",
    		description = "모든 사용자용 TBTI 질문을 반환합니다.",
			parameters = {
					@Parameter(name = "categories", description = "TBTI 특성 카테고리입니다.")
    		},
			responses = {
					@ApiResponse( responseCode = "200", description = "처리 성공시 반환되는 TBTI 질문 Page입니다.",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = TbtiQuestionResponse.class)) // Generic 반환의 경우, 정확한 표시를 위해서는 별도의 class로 재정의 필요
					), 
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
    public ResponseEntity<?> getAllTbtiQuestion(
            @RequestParam(name = "categories", required = false) String traitCategory){
        if (traitCategory == null || traitCategory.isEmpty())
            return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestion()));
        return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestionByTraitCategory(traitCategory)));
    }
}
