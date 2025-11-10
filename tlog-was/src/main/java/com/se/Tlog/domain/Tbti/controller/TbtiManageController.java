package com.se.Tlog.domain.Tbti.controller;

import com.se.Tlog.domain.Tbti.application.TbtiManagerService;
import com.se.Tlog.domain.Tbti.controller.dto.*;
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

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/tbti")
@RequiredArgsConstructor
@Tag(name = "TBTI 관리 도메인")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TbtiManageController {
    private final TbtiManagerService tbtiService;

	@GetMapping("/user/question")
	@Operation (
			summary = "전체 TBTI 질문 조회하기",
			description = "TBTI 질문을 조회합니다."
					+ "<br> 가중치는 1~5 사이의 값을 갖습니다."
					+ "<br> Percentage는 0~99 사이의 값을 갖습니다. (TBTI 코드 규칙을 따름)"
					+ "<br> TBTI 카테고리는 [RISK_TAKING, LOCATION_PREFERENCE, PLANNING_STYLE,  ACTIVITY_LEVEL] 의 값을 갖습니다.",
			responses = {
					@ApiResponse( responseCode = "200", description = "성공"),
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
	public ResponseEntity<SuccessRes<List<RawTbtiQuestionRes>>> getAllTbtiQuestion(){
		return ResponseEntity.ok(SuccessRes.from(
						tbtiService.getAllRawQeustions()));
	}

    @PostMapping("/user/question")
    @Operation (
    		summary = "새 TBTI 질문 등록하기",
    		description = "새 TBTI 질문을 등록합니다."
    		            + "<br> 가중치는 1~5 사이의 값을 갖습니다."
						+ "<br> Percentage는 0~99 사이의 값을 갖습니다. (TBTI 코드 규칙을 따름)"
    		            + "<br> TBTI 카테고리는 [RISK_TAKING, LOCATION_PREFERENCE, PLANNING_STYLE,  ACTIVITY_LEVEL] 의 값을 갖습니다.",
			responses = {
					@ApiResponse( responseCode = "200", description = "성공"), 
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
    public ResponseEntity<SuccessRes<RawTbtiQuestionRes>> createTbtiQuestion(@RequestBody TbtiQuestionReq tbtiQuestionReq){
        return ResponseEntity.ok(SuccessRes.from(
						tbtiService.createTbtiQuestion(tbtiQuestionReq)));
    }

	@PutMapping("/user/question")
	@Operation (
			summary = "TBTI 질문 수정하기",
			description = "기존의 TBTI 질문을 수정합니다."
					    + "<br> 가중치는 1~5 사이의 값을 갖습니다."
						+ "<br> TBTI 카테고리는 [RISK_TAKING, LOCATION_PREFERENCE, PLANNING_STYLE,  ACTIVITY_LEVEL] 의 값을 갖습니다."
					    + "<br> 수정된 후의 질문 데이터가 반환됩니다.",
			responses = {
					@ApiResponse(responseCode = "200", description = "성공"),
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
	public ResponseEntity<SuccessRes<RawTbtiQuestionRes>> updateTbtiQuestion(@RequestBody PutQuestionReq tbtiQuestionReq){
		return ResponseEntity.ok(
				SuccessRes.from(
						tbtiService.updateQuestion(tbtiQuestionReq)));
	}

	@PutMapping("/user/answer")
	@Operation (
			summary = "TBTI 응답 추가/수정하기",
			description = "기존의 TBTI 응답을 추가/수정합니다."
					    + "<br> id를 -1로 기입하면 응답이 추가됩니다."
						+ "<br> Percentage는 0~99 사이의 값을 갖습니다. (TBTI 코드 규칙을 따름)"
						+ "<br> 수정된 후의 질문 데이터가 반환됩니다.",
			responses = {
					@ApiResponse(responseCode = "200", description = "성공"),
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
	public ResponseEntity<SuccessRes<RawTbtiQuestionRes>> updateTbtiAnswer(@RequestBody PutAnswerReq tbtiAnswerReq){
		return ResponseEntity.ok(
				SuccessRes.from(
						tbtiService.updateOrAddAnswer(tbtiAnswerReq)));
	}

	@DeleteMapping("/user/answer/{tbtiAnswerId}")
	@Operation (
			summary = "TBTI 응답 삭제하기",
			description = "기존의 TBTI 응답을 삭제합니다.",
			responses = {
					@ApiResponse(responseCode = "200", description = "성공"),
					@ApiResponse(responseCode = "505", description = "서버 내부 오류")}
	)
	public ResponseEntity<SuccessRes<?>> deleteTbtiAnswer(@PathVariable long tbtiAnswerId){
		tbtiService.deleteAnswer(tbtiAnswerId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
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
