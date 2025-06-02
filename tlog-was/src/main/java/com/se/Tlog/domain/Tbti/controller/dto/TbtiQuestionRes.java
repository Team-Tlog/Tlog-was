package com.se.Tlog.domain.Tbti.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.Tbti.domain.TraitCategory;

@Schema(description = "TBTI 질문 응답 DTO")
public record TbtiQuestionRes(
		
		@Schema(description = "TBTI 질문의 고유 UUID")
        UUID id,
        
        @Schema(description = "TBTI 질문의 표시 내용")
        String content,
        
        @Schema(description = "TBTI 질문의 가중치")
        int weight,
        
        @Schema(description = "질문지의 TBTI 영역")
        TraitCategory traitCategory,
        
        @Schema(description = "질문지의 TBTI 영역 알파벳")
	    String categoryIntial,
	    
	    @Schema(description = "응답 형식")
	    List<TbtiAnswerDto> answers) {
    
}
