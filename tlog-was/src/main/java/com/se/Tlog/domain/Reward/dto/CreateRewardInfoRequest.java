package com.se.Tlog.domain.Reward.dto;

import com.se.Tlog.domain.Reward.Entity.RewardCriteriaType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "새 보상 형식의 생성 요청을 위한 정보입니다.")
public record CreateRewardInfoRequest(
		@Schema(description = "보상의 이름")
		String name,
		
		@Schema(description = "보상 달성 조건의 유형")
		RewardCriteriaType criteriaType,
		
		@Schema(description = "보상 달성 조건의 세부 설정")
		String criteriaParameter) {
	
}
