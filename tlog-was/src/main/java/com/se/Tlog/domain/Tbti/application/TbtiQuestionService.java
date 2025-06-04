package com.se.Tlog.domain.Tbti.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiAnswerDto;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiQuestionReq;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiQuestionRes;
import com.se.Tlog.domain.Tbti.domain.TbtiAnswer;
import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import com.se.Tlog.domain.Tbti.repository.jpa.TbtiQuestionRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class TbtiQuestionService {

    private final TbtiQuestionRepository tbtiQuestionRepository;

    @Transactional
    public void createTbtiQuestion(TbtiQuestionReq tbtiQuestionReq) {
        if (tbtiQuestionReq.answers() == null || tbtiQuestionReq.answers().size() == 0)
            throw new CustomException(ErrorType.QUESTION_HAS_NO_ANSWER);

        List<TbtiAnswer> tbtiAnswers = tbtiQuestionReq.answers().stream()
                .map(dto -> TbtiAnswer.createAnswer(dto.content(), dto.percentage()))
                .toList();
        TbtiQuestion tbtiQuestion = TbtiQuestion.create(
                tbtiQuestionReq.content(),
                TraitCategory.fromString(tbtiQuestionReq.traitCategory()),
                tbtiAnswers,
                tbtiQuestionReq.weight());

        tbtiQuestionRepository.save(tbtiQuestion);
    }

    public List<TbtiQuestionRes> getAllTbtiQuestion() {
        List<TbtiQuestion> questions = tbtiQuestionRepository.findAllFetch();
        return questions.stream().map(question -> new TbtiQuestionRes(
                question.getId(),
                question.getContent(),
                question.getAnswerWeight(),
                question.getTraitCategory(),
                question.getTraitCategory().getCategoryInitial(),
                question.getTbtiAnswers().stream()
                        .map(answer -> new TbtiAnswerDto(answer.getContent(), answer.getPercentage()))
                        .toList())
                ).toList();
    }

    public List<TbtiQuestionRes> getAllTbtiQuestionByTraitCategory(String traitCategory) {
        TraitCategory traitCategoryEnum = TraitCategory.fromString(traitCategory);
        List<TbtiQuestion> questions = tbtiQuestionRepository.findByTraitCategoryFetch(traitCategoryEnum);
        return questions.stream().map(question -> new TbtiQuestionRes(
                question.getId(),
                question.getContent(),
                question.getAnswerWeight(),
                question.getTraitCategory(),
                question.getTraitCategory().getCategoryInitial(),
                question.getTbtiAnswers().stream()
                        .map(answer -> new TbtiAnswerDto(answer.getContent(), answer.getPercentage()))
                        .toList())
                ).toList();
    }

    @Transactional
    public void deleteTbtiQuestion(UUID tbtiQuestionId) {
        TbtiQuestion tbtiQuestion = tbtiQuestionRepository.findById(tbtiQuestionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        tbtiQuestionRepository.delete(tbtiQuestion);
    }
}
