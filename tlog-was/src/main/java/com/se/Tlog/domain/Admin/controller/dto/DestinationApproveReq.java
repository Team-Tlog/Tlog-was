package com.se.Tlog.domain.Admin.controller.dto;

import java.util.List;

import com.se.Tlog.domain.Travel.controller.dto.AddFixedTagDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행지 검수 완료 처리의 데이터 형식입니다.")
public record DestinationApproveReq(
        @Schema(description = "검수할 여행지의 ID입니다.")
        String destinationId,
        
        @Schema(description = "여행지에 추가할 고유 태그 정보(커스텀 태그가 아님)입니다.")
        List<AddFixedTagDto> fixedTags) {

}
