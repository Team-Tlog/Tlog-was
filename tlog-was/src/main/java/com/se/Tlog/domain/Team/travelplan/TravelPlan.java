package com.se.Tlog.domain.Team.travelplan;

import com.se.Tlog.domain.Team.controller.dto.TravelPlanDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Document(collection = "travelPlans")
@NoArgsConstructor
@Getter
public class TravelPlan {


    private String teamId;

    private String city;

    private boolean hasPet;

    private boolean hasTransport;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> region = new ArrayList<>();

    private Map<Integer, Integer> visitCountPerDay = new HashMap<>();

    private TravelPlan(String teamId,String city, List<String> region, boolean hasPet, boolean hasTransport,
                       LocalDate startDate, LocalDate endDate, Map<Integer, Integer> visitCountPerDay) {
        this.teamId = teamId;
        this.city = city;
        this.region = new ArrayList<>(region);
        this.hasPet = hasPet;
        this.hasTransport = hasTransport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.visitCountPerDay = new HashMap<>(visitCountPerDay);
    }

    public static TravelPlan of(String teamId,TravelPlanDto dto) {
        if (dto.startDate() != null && dto.endDate() != null && dto.startDate().isAfter(dto.endDate())) {
            throw new CustomException(ErrorType.TRAVEL_PLAN_INVALID_DATE);
        }

        return new TravelPlan(
                teamId,
                Optional.ofNullable(dto.city()).orElse(""),
                Optional.ofNullable(dto.regionList()).orElse(new ArrayList<>()),
                dto.hasPet(),
                dto.hasTransport(),
                dto.startDate(),
                dto.endDate(),
                Optional.ofNullable(dto.visitCountPerDay()).orElse(new HashMap<>())
        );
    }
}