package com.se.Tlog.domain.Course.controller.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public record DailyScheduleReq(
        int dayNumber,
        LocalDate date,

        @NotEmpty(message = "해당 날짜에 여행지가 하나도 없습니다.")
        List<String> destinationIds
) {
}
