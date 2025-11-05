package com.se.Tlog.domain.Travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행지에 태그를 추가하는 요청 DTO입니다.")
public record AddFixedTagDto(
        @Schema(description = "추가하려는 태그의 id입니다.")
        String tagId,
        
        @Schema(description = "태그 가중치")
        double tagWeight) {

}
