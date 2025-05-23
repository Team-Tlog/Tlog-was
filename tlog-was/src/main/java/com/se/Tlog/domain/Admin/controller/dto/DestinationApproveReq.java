package com.se.Tlog.domain.Admin.controller.dto;

import java.util.List;

import com.se.Tlog.domain.Travel.controller.dto.AddFixedTagDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행지 검수 완료 처리의 데이터 형식입니다.")
public record DestinationApproveReq(
        @Schema(description = "(여행지 id가 아닌) 미검수 데이터에 대한 고유 id입니다.")
        String id,
        
        @Schema(description = "여행지에 추가할 고유 태그 정보(커스텀 태그가 아님)입니다.")
        List<AddFixedTagDto> fixedTags) {

}
