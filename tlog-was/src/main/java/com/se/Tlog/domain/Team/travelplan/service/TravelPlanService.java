package com.se.Tlog.domain.Team.travelplan.service;

import com.se.Tlog.domain.Team.controller.dto.TravelPlanDto;
import com.se.Tlog.domain.Team.travelplan.TravelPlan;
import com.se.Tlog.domain.Team.travelplan.repository.mongo.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    public void saveTravelPlan(UUID teamId, TravelPlanDto travelPlanDto) {
        TravelPlan travelPlan = TravelPlan.of(teamId.toString(), travelPlanDto);
        travelPlanRepository.save(travelPlan);
    }
}
