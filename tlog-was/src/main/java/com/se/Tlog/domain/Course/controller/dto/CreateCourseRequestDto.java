package com.se.Tlog.domain.Course.controller.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행 코스 생성 데이터 규격입니다.")
public record CreateCourseRequestDto(
        LocalDate startDate,
        LocalDate endDate,
        
        @Schema(description = "날짜별로 구분된 여행지 리스트입니다.")
        List<List<String>> destinationIds
        ) {

}
