package com.se.Tlog.domain.Course.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CourseSaveReq(

        @NotNull(message = "시작 날짜는 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "종료 날짜는 필수입니다.")
        LocalDate endDate,

        @NotEmpty(message = "일자별 스케줄이 비어있습니다.")
        @Valid
        List<DailyScheduleReq> dailySchedules
) {
    public record DailyScheduleReq(
            int dayNumber,
            LocalDate date,

            @NotEmpty(message = "해당 날짜에 여행지가 하나도 없습니다.")
            List<String> destinationIds // 사용자가 선택한 최종 여행지 ID 목록
    ) {}
}