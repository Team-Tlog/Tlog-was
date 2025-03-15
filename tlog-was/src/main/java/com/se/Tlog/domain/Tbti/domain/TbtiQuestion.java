package com.se.Tlog.domain.Tbti.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public TbtiQuestion(String content, TraitCategory traitCategory) {
        validateContent(content);
        this.content = content;
        this.traitCategory = traitCategory;
    }

    // 질문 생성 메서드
    public static TbtiQuestion create(String content, TraitCategory traitCategory) {
        return new TbtiQuestion(content, traitCategory);
    }

    // 유효성 검사 메서드
    private void validateContent(String content){
        if(content == null || content.isBlank()){
            throw new CustomException(ErrorType.CONTENT_NOT_FOUND);
        }
        // 필요한 검사가 있다면 지속적으로 추가할 예정!
    }

    public void delete(){

    }



}
