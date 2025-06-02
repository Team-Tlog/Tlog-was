package com.se.Tlog.domain.Tbti.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TbtiAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "tbtiQuestion_id")
    private TbtiQuestion tbtiQuestion;
    
    private String content;

    private int percentage;
    
    public void setQuestion(TbtiQuestion question) {
        this.tbtiQuestion = question;
    }

    @Builder
    private TbtiAnswer(String content, int percentage) {
        this.content = content;
        this.percentage = percentage;
    }

    //응답 생성 메서드
    public static TbtiAnswer createAnswer(String content, int percentage) {
        if (content == null)
            throw new CustomException(ErrorType.CONTENT_NOT_FOUND);
        if (percentage < 0 || percentage > 99)
            throw new CustomException(ErrorType.INVALID_ANSWER_PERCENTAGE);
        
        return TbtiAnswer.builder()
                .content(content)
                .percentage(percentage)
                .build();
    }
}
