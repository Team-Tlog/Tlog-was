package com.se.Tlog.domain.Tbti.Entity;

import com.se.Tlog.domain.User.Entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TbtiAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tbtiQuestion_id")
    private TbtiQuestion tbtiQuestion;

    private int score;

    @Builder
    public TbtiAnswer(User user, TbtiQuestion tbtiQuestion, int score) {
        this.user = user;
        this.tbtiQuestion = tbtiQuestion;
        this.score = score;
    }

    //응답 생성 메서드
    public static TbtiAnswer createAnswer(User user, TbtiQuestion tbtiQuestion, int score) {
        return TbtiAnswer.builder()
                .user(user)
                .tbtiQuestion(tbtiQuestion)
                .score(score)
                .build();
    }
}
