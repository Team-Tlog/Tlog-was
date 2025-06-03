package com.se.Tlog.domain.Tbti.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TBTI 질문 요청 DTO")
public record TbtiQuestionReq(

	@Schema(description = "질문의 내용입니다.")
    String content,
    
    @Schema(description = "질문의 가중치입니다. (1~5)")
    int weight,
	
	@Schema(description = "TBTI 유형입니다. (RISK_TAKING, LOCATION_PREFERENCE, PLANNING_STYLE,  ACTIVITY_LEVEL)")
    String traitCategory,
    
    @Schema(description = "응답 형식")
    List<TbtiAnswerDto> answers) {
	
}
