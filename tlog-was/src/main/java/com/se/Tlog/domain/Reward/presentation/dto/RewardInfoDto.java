package com.se.Tlog.domain.Reward.presentation.dto;

import com.se.Tlog.domain.Reward.domain.RewardCriteriaType;
import com.se.Tlog.domain.Reward.domain.RewardInfo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "보상 형식을 나타내는 DTO")
public record RewardInfoDto(
		@Schema(description = "보상 형식의 고유 ID")
		Long id,
		
		@Schema(description = "보상 형식의 이름")
		String name,
		
		@Schema(description = "보상 형식의 달성 조건 형식")
		RewardCriteriaType criteriaType,
		
		@Schema(description = "보상 형식의 달성 조건 매개변수")
		String criteriaParameter
		) {
	public static RewardInfoDto fromEntity(RewardInfo entity) {
		return new RewardInfoDto(
				entity.getId(), 
				entity.getName(), 
				entity.getRewardCriteria().getCriteriaType(), 
				entity.getRewardCriteria().getCriteriaParameter());
	}
}
