package com.se.Tlog.domain.Team.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.Team.domain.Team;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 정보를 표시하는 DTO입니다.")
public record TeamResponseDto(
		UUID teamId,
		String teamName,
		String teamLeaderName,
		List<TeamMemberSimpleDto> memberSimpleDtoList, //유저들 id + profile 예정
		TravelPlanDto travelPlanDto,
		LocalDate createAt
		) {
	public static TeamResponseDto from(Team team, String teamLeaderName, List<TeamMemberSimpleDto> memberSimpleDtoList,TravelPlanDto travelPlanDto) {
		return new TeamResponseDto(
				team.getId(),
				team.getName(),
				teamLeaderName,
				memberSimpleDtoList,
				travelPlanDto,
				team.getCreatedAt().toLocalDate()
		);
	}
}
