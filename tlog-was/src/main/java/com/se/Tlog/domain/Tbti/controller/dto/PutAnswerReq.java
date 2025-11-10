package com.se.Tlog.domain.Tbti.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class PutAnswerReq {
    private UUID questionId;
    private long answerId;
    @Schema(example = "아니, 난 도시가 좋아!")
    private String content;
    @Schema(example = "99")
    private int percentage;
}
