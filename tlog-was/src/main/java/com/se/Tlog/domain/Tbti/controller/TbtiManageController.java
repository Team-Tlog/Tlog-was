package com.se.Tlog.domain.Tbti.controller;

import com.se.Tlog.domain.Tbti.application.TbtiQuestionService;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiQuestionReq;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/tbti")
@RequiredArgsConstructor
@Tag(name = "TBTI 관리 도메인")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TbtiManageController {
    private final TbtiQuestionService tbtiService;

    @PostMapping("/user/question")
    @Operation (
    		summary = "새 TBTI 질문 등록하기",
    		description = "새 TBTI 질문을 등록합니다."
    		            + "<br> 가중치는 1~5 사이의 값을 갖습니다."
    		            + "<br> TBTI 카테고리는 [RISK_TAKING, LOCATION_PREFERENCE, PLANNING_STYLE,  ACTIVITY_LEVEL] 의 값을 갖습니다.",
			responses = {
					@ApiResponse( responseCode = "200", description = "성공"), 
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
    public ResponseEntity<?> createTbtiQuestion(@RequestBody TbtiQuestionReq tbtiQuestionReq){
        tbtiService.createTbtiQuestion(tbtiQuestionReq);

        return ResponseEntity.ok()
                .body(SuccessRes.from(SuccessType.OK));
    }
    
    @DeleteMapping("/user/question/{tbtiQuestionId}")
    @Operation (
    		summary = "TBTI 질문 삭제하기",
    		description = "TBTI 질문을 삭제합니다.",
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
