package com.se.Tlog.domain.Course.controller.dto;

import java.util.List;

import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 이름으로 그룹화된 여행지 집합을 나타냅니다.")
public record DestinationGroupDto(
        @Schema(description = "그룹 이름. 지역 기준으로 그룹화될 경우 지역 이름이 표시됩니다.")
        String groupName,
        
        @Schema(description = "그룹에 속한 여행지 목록")
        List<DestinationSummaryRes> destinations) {
}
