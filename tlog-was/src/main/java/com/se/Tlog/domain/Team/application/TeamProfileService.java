package com.se.Tlog.domain.Team.application;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class TeamProfileService {
    private final TeamRepository teamRepository;
    
    @Transactional
    public String updateTeamTbti(UUID teamId, int tbtiCode) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
        team.setTbti(new Tbti(tbtiCode));
        return team.getTbtiString();
    }
}
