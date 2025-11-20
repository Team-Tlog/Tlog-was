package com.se.Tlog.domain.Course.controller.dto;

import com.se.Tlog.domain.Course.domain.OwnerType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
public record CourseDailyGroupedRes(
        String id,
        UUID ownerId,
        OwnerType ownerType,
        LocalDate startDate,
        LocalDate endDate,
        int duration,
        List<DailySchedule> dailySchedules
) {
    @Builder
    public record DailySchedule(
            int dayNumber,
            Map<String, List<RecommendedDestinationDto>> groupedDestinations
    ) {}
}
