package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.Team.travelplan.TravelPlan;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Schema(description = "여행 계획 정보 DTO")
public record TravelPlanDto(

        @Schema(description = "여행 도시")
        String city,

        @Schema(description = "여행 지역")
        List<String> regionList,

        @Schema(description = "반려견 동반 여부")
        boolean hasPet,

        @Schema(description = "이동수단 사용 여부")
        boolean hasTransport,

        @Schema(description = "여행 시작일")
        LocalDate startDate,

        @Schema(description = "여행 종료일")
        LocalDate endDate,

        @Schema(description = "일자별 방문 여행지 수 설정")
        Map<Integer, Integer> visitCountPerDay
) {
        public static TravelPlanDto from(TravelPlan travelPlan) {
                return new TravelPlanDto(
                        travelPlan.getCity(),
                        travelPlan.getRegion() != null ? List.copyOf(travelPlan.getRegion()) : List.of(),
                        travelPlan.isHasPet(),
                        travelPlan.isHasTransport(),
                        travelPlan.getStartDate(),
                        travelPlan.getEndDate(),
                        travelPlan.getVisitCountPerDay() != null ? Map.copyOf(travelPlan.getVisitCountPerDay()) : Map.of()
                );
        }
}
