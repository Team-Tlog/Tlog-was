package com.se.Tlog.domain.Team.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 생성에 요구되는 DTO 형식입니다.")
public record CreateTeamRequestDto(
		@Schema(description = "팀 이름")
		String name,
		
		@Schema(description = "팀 생성자")
		UUID creator //,
		
		//@Schema(description = "팀 TBTI")
		//String teamTbti
		) {

}
