package com.se.Tlog.domain.Team.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기본적인 팀원 관리 요청에 사용되는 DTO. 팀 초대, 팀원으로 추가, 팀 방출에 사용됩니다.")
public record TeamUserRequestDto(
		@Schema(description = "처리할 팀의 초대 코드")
		String inviteCode,
		
		@Schema(description = "처리할 유저의 id")
		UUID userId
		) {

}
