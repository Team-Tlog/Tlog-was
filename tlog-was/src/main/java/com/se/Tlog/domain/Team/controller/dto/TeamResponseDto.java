package com.se.Tlog.domain.Team.controller.dto;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 정보를 표시하는 DTO입니다.")
public record TeamResponseDto(
		UUID teamId,
		String teamName,
		List<UUID> memberIdList
		) {

}
