package com.se.Tlog.domain.Tbti.controller.dto;

import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class PutQuestionReq {
    private UUID id;
    @Schema(example = "시골이 좋아 도시가 좋아?")
    private String content;
    private TraitCategory traitCategory;
    @Schema(example = "3")
    private int weight;
}
