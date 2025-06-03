package com.se.Tlog.domain.tlog;

import com.se.Tlog.domain.Tbti.domain.TbtiAnswer;
import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;

import java.util.List;


@SpringBootTest
@Slf4j
public class TbtiQuestionTest {

    String content = "너는 여행을 계획할 때 활동적인게 좋아?";
    TraitCategory traitCategory = TraitCategory.ACTIVITY_LEVEL;
    TbtiQuestion tbtiQuestion = TbtiQuestion.create(content, traitCategory, List.of(TbtiAnswer.createAnswer("응답 1", 50)), 1);


    @Test
    @DisplayName("질문 생성 테스트")
    void tbtiQuestionTest(){

        log.info("Starting creation test with content: '{}' and traitCategory: {}", content, traitCategory);

        assertThat(tbtiQuestion.getContent()).isEqualTo(content);
        assertThat(tbtiQuestion.getTraitCategory()).isEqualTo(traitCategory);
    }

    /*
     * 25.05.31
     * TbtiAnswer 엔티티의 용도가 변경됨에 따라 테스트 로직이 수정되었습니다.
     * 
    @Test
    @DisplayName("질문 응답 테스트")
    void tbtiAnswerTest() {
    	// User 객체의 생성 로직 설계중임에 따라,
    	// 임시로 로직 변경
    	// -> (소셜 로그인 형태로 User 생성하도록 임시조치)
    	User user = User.create(
    			new SsoUserInfo(
    					"TEST_PROVIDER_ID", 
		    			"aaa@naver.com", 
		    			"강진후",
		    			"TEST_PROVIDER"));
    	// phoneNumber : "010-2222-2222"
    	
        int score = 3;
        TbtiAnswer tbtiAnswer = TbtiAnswer.createAnswer(user,tbtiQuestion,score);
        log.info("creation answer with score: '{}' and question content: '{}'", score, tbtiQuestion.getContent());

        assertThat(tbtiAnswer.getTbtiQuestion().getContent()).isEqualTo(content);
        assertThat(tbtiAnswer.getScore()).isEqualTo(score);
        assertThat(tbtiAnswer.getUser()).isEqualTo(user);

    }*/
}
