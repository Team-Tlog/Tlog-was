package com.se.Tlog.domain.Admin.controller.dto;

import java.util.UUID;

import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "검수되지 않은 여행지 데이터입니다.")
public record UnapprovedDestinationDto(
        @Schema(description = "(여행지 id가 아닌) 미검수 데이터에 대한 고유 id입니다.")
        String id,
        
        @Schema(description = "여행지 생성 요청자 id입니다.")
        UUID submitter,
        
        @Schema(description = "내부 여행지 데이터입니다.")
        DestinationSummaryRes destination
        ) {

}
