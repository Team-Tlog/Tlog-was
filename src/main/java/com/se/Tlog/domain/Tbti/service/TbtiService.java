package com.se.Tlog.domain.Tbti.service;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import com.se.Tlog.domain.Tbti.Entity.repository.TbtiQuestionRepository;
import com.se.Tlog.domain.Tbti.dto.TbtiQuestionReq;
import com.se.Tlog.domain.Tbti.dto.TbtiQuestionRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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
}
