package com.se.Tlog.domain.Reward.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자의 보상 데이터를 표시하는 형식입니다.")
public record UserRewardDto(
        Long rewardId, // 서비스의 고유 보상 id인 RewardInfo의 id입니다!
        String name,
        String description,
        String iconImageUrl) {

}
