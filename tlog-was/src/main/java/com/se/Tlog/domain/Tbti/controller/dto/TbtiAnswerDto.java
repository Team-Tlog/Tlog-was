package com.se.Tlog.domain.Tbti.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TBTI 질문의 응답 형식 DTO")
public record TbtiAnswerDto(
        @Schema(description = "응답 문구")
        String content,
        
        @Schema(description = "해당하는 TBTI 성향의 % 값 (0~99)")
        int percentage) {

}
