package com.se.Tlog.domain.Tbti.dto;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "TBTI 질문 요청 DTO")
public class TbtiQuestionReq {

	@Schema(description = "질문의 내용입니다.")
    String content;
	
	@Schema(description = "TBTI 유형입니다.")
    TraitCategory traitCategory;

    public static TbtiQuestion toEntity(TbtiQuestionReq tbtiQuestionReq){
        return TbtiQuestion.create(tbtiQuestionReq.getContent(), tbtiQuestionReq.getTraitCategory());
    }
}
