package com.se.Tlog.domain.Course.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record DayPlanDto(

        @NotNull(message = "날짜가 누락되었습니다.")
        @FutureOrPresent(message = "날짜는 오늘 또는 미래여야 합니다.")
        LocalDate date,

        @NotNull(message = "방문지 개수가 누락되었습니다.")
        @Min(value = 0, message = "방문지 개수는 0개 이상이어야 합니다.")
        Integer placeCount
) {


}
