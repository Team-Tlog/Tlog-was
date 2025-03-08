package com.se.Tlog.domain.Tbti.dto;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "TBTI 질문 응답 DTO")
public record TbtiQuestionRes(
		
		@Schema(description = "TBTI 질문의 고유 UUID")
        UUID id,
        
        @Schema(description = "TBTI 질문의 표시 내용")
        String content,
        
        @Schema(description = "질문지의 TBTI 영역")
        TraitCategory traitCategory
) {
    public static TbtiQuestionRes from(TbtiQuestion tbtiQuestion) {
        return new TbtiQuestionRes(
                tbtiQuestion.getId(),
                tbtiQuestion.getContent(),
                tbtiQuestion.getTraitCategory()
        );
    }
}
