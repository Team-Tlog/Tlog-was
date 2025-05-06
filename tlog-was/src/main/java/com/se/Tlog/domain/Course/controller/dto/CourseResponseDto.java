package com.se.Tlog.domain.Course.controller.dto;

import java.time.LocalDate;
import java.util.List;

import com.se.Tlog.domain.Course.domain.Course;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행 코스 데이터입니다.")
public record CourseResponseDto(
        @Schema(description = "코스 데이터의 고유 id")
        String id,
        
        @Schema(description = "코스의 1일차 날짜. ISO 8601(YYYY-MM-DD)규격을 따릅니다.")
        LocalDate startDate,
        
        @Schema(description = "코스의 마지막 날짜. ISO 8601(YYYY-MM-DD)규격을 따릅니다.")
        LocalDate endDate,
    
        @Schema(description = "날짜별 여행지 데이터")
        List<CourseDateDto> dates
        ) {
    
    public static CourseResponseDto from(Course course, List<CourseDateDto> courseDates) {
        return new CourseResponseDto(
                course.getId(), 
                course.getStartDate(), 
                course.getEndDate(),
                courseDates);
    }
}
