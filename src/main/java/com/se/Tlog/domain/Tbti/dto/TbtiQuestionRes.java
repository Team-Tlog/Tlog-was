package com.se.Tlog.domain.Tbti.dto;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;

import java.util.UUID;


public record TbtiQuestionRes(
        UUID id,
        String content,
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
