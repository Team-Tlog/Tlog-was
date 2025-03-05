package com.se.Tlog.domain.tlog;

import com.se.Tlog.domain.Tbti.Entity.TbtiAnswer;
import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import com.se.Tlog.domain.User.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Slf4j
public class TbtiQuestionTest {

    String content = "너는 여행을 계획할 때 활동적인게 좋아?";
    TraitCategory traitCategory = TraitCategory.ACTIVITY_LEVEL;
    TbtiQuestion tbtiQuestion = TbtiQuestion.create(content, traitCategory);


    @Test
    @DisplayName("질문 생성 테스트")
    void tbtiQuestionTest(){

        log.info("Starting creation test with content: '{}' and traitCategory: {}", content, traitCategory);

        assertThat(tbtiQuestion.getContent()).isEqualTo(content);
        assertThat(tbtiQuestion.getTraitCategory()).isEqualTo(traitCategory);
    }

    @Test
    @DisplayName("질문 응답 테스트")
    void tbtiAnswerTest() {
        User user = User.builder()
                .userId("test1")
                .email("aaa@naver.com")
                .name("강진후")
                .telephoneNumber("010-2222-2222")
                .build();
        int score = 3;
        TbtiAnswer tbtiAnswer = TbtiAnswer.createAnswer(user,tbtiQuestion,score);
        log.info("creation answer with score: '{}' and question content: '{}'", score, tbtiQuestion.getContent());

        assertThat(tbtiAnswer.getTbtiQuestion().getContent()).isEqualTo(content);
        assertThat(tbtiAnswer.getScore()).isEqualTo(score);
        assertThat(tbtiAnswer.getUser()).isEqualTo(user);

    }
}
