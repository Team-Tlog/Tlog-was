package com.se.Tlog.domain.Tbti.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiAnswerDto;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiQuestionRes;
import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import com.se.Tlog.domain.Tbti.repository.jpa.TbtiQuestionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class TbtiQuestionService {
    private final TbtiQuestionRepository tbtiQuestionRepository;

    public List<TbtiQuestionRes> getAllTbtiQuestion() {
        List<TbtiQuestion> questions = tbtiQuestionRepository.findAllFetch();
        return questions.stream().map(TbtiQuestionRes::from).toList();
    }

    public List<TbtiQuestionRes> getAllTbtiQuestionByTraitCategory(String traitCategory) {
        TraitCategory traitCategoryEnum = TraitCategory.fromString(traitCategory);
        List<TbtiQuestion> questions = tbtiQuestionRepository.findByTraitCategoryFetch(traitCategoryEnum);
        return questions.stream().map(TbtiQuestionRes::from).toList();
    }
}
