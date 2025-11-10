package com.se.Tlog.domain.Tbti.controller.dto;

import com.se.Tlog.domain.Tbti.domain.TbtiAnswer;
import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RawTbtiQuestionRes {
    private UUID id;

    @Schema(example = "분주한 도시와 한산한 자연 풍경 중에서?")
    private String content;

    @Schema(example = "3")
    private int weight;

    @Schema(example = "LOCATION_PREFERENCE")
    private TraitCategory traitCategory;

    private List<RawTbtiAnswer> answers;
    @Data
    @AllArgsConstructor
    public static class RawTbtiAnswer {
        private long id;
        @Schema(example = "분주한 도시!")
        private String content;
        @Schema(example = "99")
        private int percentage;

        public static RawTbtiAnswer from(TbtiAnswer answer) {
            return new RawTbtiAnswer(
                    answer.getId(),
                    answer.getContent(),
                    answer.getPercentage()
            );
        }
    }

    public static RawTbtiQuestionRes from(TbtiQuestion question) {
        return new RawTbtiQuestionRes(
                question.getId(),
                question.getContent(),
                question.getAnswerWeight(),
                question.getTraitCategory(),
                question.getTbtiAnswers().stream().map(RawTbtiAnswer::from).toList()
        );
    }
}
