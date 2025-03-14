package com.se.Tlog.domain.Reward.presentation.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 유저에게 보상을 지급하도록 요청하는 데이터 규격")
public record AddRewardToUserRequest(
		@Schema(description = "보상을 지급할 유저")
		UUID userId,
		
		@Schema(description = "적용할 보상")
		Long rewardInfoId) {
	
}
