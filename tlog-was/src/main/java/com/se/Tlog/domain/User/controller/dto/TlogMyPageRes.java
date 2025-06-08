package com.se.Tlog.domain.User.controller.dto;

import java.util.List;

import com.se.Tlog.domain.Reward.controller.dto.UserRewardDto;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiInfoRes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tlog 내 마이페이지 정보입니다.")
public record TlogMyPageRes(
        String username,
        String profileImageUrl,
        String snsId,
        String defaultRewardPhrase,
        
        @Schema(description = "사용자가 획득한 모든 보상입니다.")
        List<UserRewardDto> userRewards,
        
        TbtiInfoRes tbtiDescription) {

}
