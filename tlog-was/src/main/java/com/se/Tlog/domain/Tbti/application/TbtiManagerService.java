package com.se.Tlog.domain.Tbti.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.controller.dto.*;
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
public class TbtiManagerService {
    private final TbtiQuestionRepository tbtiQuestionRepository;

    @Transactional
    public RawTbtiQuestionRes createTbtiQuestion(TbtiQuestionReq tbtiQuestionReq) {
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

        TbtiQuestion savedEntity = tbtiQuestionRepository.save(tbtiQuestion);
        return RawTbtiQuestionRes.from(savedEntity);
    }

    public List<RawTbtiQuestionRes> getAllRawQeustions() {
        return tbtiQuestionRepository.findAllFetch().stream()
                .map(RawTbtiQuestionRes::from)
                .toList();
    }

    @Transactional
    public RawTbtiQuestionRes updateQuestion(PutQuestionReq dto) {
        TbtiQuestion tbtiQuestion = tbtiQuestionRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        tbtiQuestion.setContent(dto.getContent());
        tbtiQuestion.setTraitCategory(dto.getTraitCategory());
        tbtiQuestion.setAnswerWeight(dto.getWeight());
        TbtiQuestion savedEntity = tbtiQuestionRepository.save(tbtiQuestion);

        return RawTbtiQuestionRes.from(savedEntity);
    }

    @Transactional
    public RawTbtiQuestionRes updateOrAddAnswer(PutAnswerReq dto) {
        TbtiQuestion tbtiQuestion = tbtiQuestionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        TbtiAnswer answer = null;
        for (TbtiAnswer a : tbtiQuestion.getTbtiAnswers())
            if (a.getId() == dto.getAnswerId()) {
                answer = a;
                break;
            }

        if (answer == null) {
            answer = TbtiAnswer.createAnswer(
                    dto.getContent(),
                    dto.getPercentage());
            answer.setTbtiQuestion(tbtiQuestion);
            tbtiQuestion.getTbtiAnswers().add(answer);
        }
        else {
            answer.setContent(dto.getContent());
            answer.setPercentage(dto.getPercentage());
        }

        TbtiQuestion savedEntity = tbtiQuestionRepository.save(tbtiQuestion);
        return RawTbtiQuestionRes.from(savedEntity);
    }

    @Transactional
    public void deleteAnswer(long id) {
        List<TbtiQuestion> tbtiQuestions = tbtiQuestionRepository.findByAnswerId(id);

        boolean isRemoved = false;
        for (TbtiQuestion question : tbtiQuestions) {
            TbtiAnswer answer = null;
            for (TbtiAnswer a : question.getTbtiAnswers())
                if (a.getId() == id) {
                    answer = a;
                    break;
                }

            if (answer != null) {
                question.getTbtiAnswers().remove(answer);
                isRemoved = true;
            }
        }

        if (isRemoved)
            tbtiQuestionRepository.saveAll(tbtiQuestions);
    }

    @Transactional
    public void deleteTbtiQuestion(UUID tbtiQuestionId) {
        TbtiQuestion tbtiQuestion = tbtiQuestionRepository.findById(tbtiQuestionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        tbtiQuestionRepository.delete(tbtiQuestion);
    }
}
