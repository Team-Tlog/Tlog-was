package com.se.Tlog.domain.Team.controller.dto;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.Team.domain.Team;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 정보를 표시하는 DTO입니다.")
public record TeamResponseDto(
		UUID teamId,
		String teamName,
		UUID teamLeaderId,
		List<UUID> memberIdList //유저들 id + profile 예정
		) {
	public static TeamResponseDto from(Team team, UUID teamLeader, List<UUID> memberIdList) {
		return new TeamResponseDto(
				team.getId(),
				team.getName(),
				teamLeader,
				memberIdList
		);
	}
}
