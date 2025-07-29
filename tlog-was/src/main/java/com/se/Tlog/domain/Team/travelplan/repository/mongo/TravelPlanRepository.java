package com.se.Tlog.domain.Team.travelplan.repository.mongo;

import com.se.Tlog.domain.Team.travelplan.TravelPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPlanRepository extends MongoRepository<TravelPlan, String> {
    Optional<TravelPlan> findByTeamId(String teamId);
    List<TravelPlan> findAllByTeamIdIn(List<String> teamIds);
}
