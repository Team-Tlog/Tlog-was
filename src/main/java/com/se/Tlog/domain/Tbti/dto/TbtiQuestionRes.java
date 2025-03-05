package com.se.Tlog.domain.Tbti.dto;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;




public record TbtiQuestionRes(
        String content,
        TraitCategory traitCategory
) {
    public static TbtiQuestionRes from(TbtiQuestion tbtiQuestion) {
        return new TbtiQuestionRes(
                tbtiQuestion.getContent(),
                tbtiQuestion.getTraitCategory()
        );
    }
}
