package com.se.Tlog.domain.Tbti.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import com.se.Tlog.domain.Tbti.infrastructure.jpa.TbtiQuestionRepository;
import com.se.Tlog.domain.Tbti.presentation.dto.TbtiQuestionReq;
import com.se.Tlog.domain.Tbti.presentation.dto.TbtiQuestionRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class TbtiService {

    final TbtiQuestionRepository tbtiQuestionRepository;

    @Transactional
    public void createTbtiQuestion(TbtiQuestionReq tbtiQuestionReq) {
        TbtiQuestion tbtiQuestion = TbtiQuestion.create(
                tbtiQuestionReq.getContent(),
                tbtiQuestionReq.getTraitCategory()
        );

        tbtiQuestionRepository.save(tbtiQuestion);
    }

    public Page<TbtiQuestionRes> getAllTbtiQuestion(Pageable pageable) {
        return tbtiQuestionRepository.findAll(pageable).map(TbtiQuestionRes::from);
    }

    public Page<TbtiQuestionRes> getAllTbtiQuestionByTraitCategory(String traitCategory, Pageable pageable) {

        TraitCategory traitCategoryEnum = TraitCategory.fromString(traitCategory);
        return tbtiQuestionRepository.findByTraitCategory(traitCategoryEnum, pageable)
                .map(TbtiQuestionRes::from);
    }

    @Transactional
    public void deleteTbtiQuestion(UUID tbtiQuestionId) {
        TbtiQuestion tbtiQuestion = tbtiQuestionRepository.findById(tbtiQuestionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        tbtiQuestionRepository.delete(tbtiQuestion);
    }
}
