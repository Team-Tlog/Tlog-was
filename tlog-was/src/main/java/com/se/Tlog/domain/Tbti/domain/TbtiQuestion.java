package com.se.Tlog.domain.Tbti.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TbtiQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Enumerated(EnumType.STRING)
    private TraitCategory traitCategory;

    @OneToMany(mappedBy = "tbtiQuestion",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TbtiAnswer> tbtiAnswers;
    
    private int answerWeight;

    private void updateAnswers(List<TbtiAnswer> tbtiAnswers) {
        if (tbtiAnswers == null || tbtiAnswers.size() == 0)
            throw new CustomException(ErrorType.QUESTION_HAS_NO_ANSWER);

        this.tbtiAnswers = new ArrayList<TbtiAnswer>(tbtiAnswers);
        this.tbtiAnswers.forEach(answer -> answer.setQuestion(this));
    }

    // 유효성 검사 메서드
    private static void validateContent(String content){
        if(content == null || content.isBlank()){
            throw new CustomException(ErrorType.CONTENT_NOT_FOUND);
        }
        // 필요한 검사가 있다면 지속적으로 추가할 예정!
    }

    public void updateQuestion(String content, TraitCategory traitCategory, int answerWeight) {
        validateContent(content);
        if (traitCategory == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        if (answerWeight < 1 || answerWeight > 5)
            throw new CustomException(ErrorType.INVALID_QUESTION_WEIGHT);

        this.content = content;
        this.traitCategory = traitCategory;
        this.answerWeight = answerWeight;
    }

    // 질문 생성 메서드
    public static TbtiQuestion create(String content, TraitCategory traitCategory, List<TbtiAnswer> tbtiAnswers, int answerWeight) {
        TbtiQuestion question = new TbtiQuestion();
        question.updateAnswers(tbtiAnswers);
        question.updateQuestion(content, traitCategory, answerWeight);
        return question;
    }

    public void delete(){

    }



}
