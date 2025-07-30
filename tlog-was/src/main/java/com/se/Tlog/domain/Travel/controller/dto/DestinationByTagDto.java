package com.se.Tlog.domain.Travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "태그를 대표하는 여행지 데이터입니다.")
public record DestinationByTagDto(
        @Schema(description = "여행지 이름")
        String name,
        
        @Schema(description = "여행지 사진 url")
        String imageUrl,
        
        @Schema(description = "대표태그 이름")
        String tagName,
        
        @Schema(description = "대표태그 id")
        String tagId) {

}
