package com.se.Tlog.domain.Course.controller.dto;

import com.se.Tlog.domain.Course.domain.OwnerType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CourseDetailRes(
        String id,
        UUID ownerId,
        OwnerType ownerType,
        LocalDate startDate,
        LocalDate endDate,
        int duration,
        List<DailyScheduleReq> dailySchedules
) {
}
