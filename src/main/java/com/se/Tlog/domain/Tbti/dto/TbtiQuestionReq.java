package com.se.Tlog.domain.Tbti.dto;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TbtiQuestionReq {

    String content;
    TraitCategory traitCategory;

    public static TbtiQuestion toEntity(TbtiQuestionReq tbtiQuestionReq){
        return TbtiQuestion.create(tbtiQuestionReq.getContent(), tbtiQuestionReq.getTraitCategory());
    }
}
