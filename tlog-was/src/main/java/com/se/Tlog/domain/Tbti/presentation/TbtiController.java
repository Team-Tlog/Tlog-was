package com.se.Tlog.domain.Tbti.presentation;

import com.se.Tlog.domain.Tbti.application.TbtiService;
import com.se.Tlog.domain.Tbti.presentation.dto.TbtiQuestionReq;
import com.se.Tlog.domain.Tbti.presentation.dto.TbtiQuestionResponse;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/tbti")
@RequiredArgsConstructor
public class TbtiController {

    final TbtiService tbtiService;

    @GetMapping
    @Operation (
    		summary = "전체 TBTI 질문 가져오기",
    		description = "모든 TBTI 질문을 반환합니다.",
			tags = {"TBTI 도메인"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			parameters = {
					@Parameter(name = "categories", description = "TBTI 특성 카테고리입니다."),
					@Parameter(name = "pageable", description = "Page 정보")
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
            @RequestParam(name = "categories", required = false) String traitCategory,
            @PageableDefault(size = 10) Pageable pageable
    ){
        if (traitCategory == null || traitCategory.isEmpty())
            return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestion(pageable)));
        return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestionByTraitCategory(traitCategory, pageable)));
    }

    @PostMapping
    @Operation (
    		summary = "새 TBTI 질문 등록하기",
    		description = "새 TBTI 질문을 등록합니다.",
			tags = {"TBTI 도메인"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse( responseCode = "200", description = "성공"), 
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
    public ResponseEntity<?> createTbtiQuestion(@RequestBody TbtiQuestionReq tbtiQuestionReq){
        tbtiService.createTbtiQuestion(tbtiQuestionReq);

        return ResponseEntity.ok()
                .body(SuccessRes.from(SuccessType.OK));
    }
    
    @DeleteMapping("/{tbtiQuestionId}")
    @Operation (
    		summary = "TBTI 질문 삭제하기",
    		description = "TBTI 질문을 삭제합니다.",
			tags = {"TBTI 도메인"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			parameters = {@Parameter(name = "tbtiQuestionId", description = "삭제할 TBTI 질문의 UUID입니다.")},
			responses = {
					@ApiResponse( responseCode = "200", description = "성공"), 
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
    public ResponseEntity<?> deleteTbtiQuestion(@PathVariable(name = "tbtiQuestionId") UUID tbtiQuestionId) {
        tbtiService.deleteTbtiQuestion(tbtiQuestionId);
        return ResponseEntity.ok()
                .body(SuccessRes.from(SuccessType.OK));
    }



}
