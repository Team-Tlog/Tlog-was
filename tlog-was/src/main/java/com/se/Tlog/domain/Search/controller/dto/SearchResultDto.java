package com.se.Tlog.domain.Search.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행지 검색 결과를 나타내는 DTO입니다.")
public record SearchResultDto(
		@Schema(description = "여행지 id")
		String id,
		
		@Schema(description = "여행지 이름")
		String name,
		
		@Schema(description = "여행지 주소")
		String address
		) {
}
