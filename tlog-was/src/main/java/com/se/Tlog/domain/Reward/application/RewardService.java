package com.se.Tlog.domain.Reward.application;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Reward.controller.dto.UserRewardDto;
import com.se.Tlog.domain.Reward.domain.Reward;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;
import com.se.Tlog.domain.Reward.repository.jpa.RewardRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class RewardService {
	private final RewardRepository rewardRepository;
	private final RewardInfoRepository rewardInfoRepository;
	private final UserRepository userRepository;
	
	/**
	 * 특정 유저에게 보상을 설정합니다.
	 * @param userId
	 * @param rewardInfoId
	 * @return
	 */
	public void addRewardToUser(UUID userId, Long rewardInfoId) {
        if (rewardRepository.existsByRewardInfo_IdAndUser_Id(rewardInfoId, userId))
            throw new CustomException(ErrorType.ALREADY_OWN_REWARD);
        User user = userRepository.findById(userId).orElseThrow(() -> 
                new CustomException(ErrorType.USER_NOT_FOUND));
        RewardInfo rewardInfo = rewardInfoRepository.findById(rewardInfoId).orElseThrow(() -> 
                new CustomException(ErrorType.NOT_FOUND));
		
		rewardRepository.save(Reward.create(user, rewardInfo));
	}
	
	/**
	 * 특정 유저가 기본으로 표시할 보상을 설정합니다.
	 * @param userId
	 * @param rewardInfoId
	 */
	@Transactional
	public void setDefaultReward(UUID userId, Long rewardInfoId) {
	    if (!userRepository.existsById(userId))
	        throw new CustomException(ErrorType.USER_NOT_FOUND);
	    
        if (!rewardRepository.existsByRewardInfo_IdAndUser_Id(rewardInfoId, userId))
            throw new CustomException(ErrorType.REWARD_NOT_RECEIVED);
	    
        rewardRepository.updateDefaultReward(rewardInfoId, userId);
	}
	
	/**
	 * 특정 유저가 보유하고 있는 모든 보상을 조회합니다.
	 * @param userId
	 * @return
	 */
	public List<UserRewardDto> getAllRewardOfUser(UUID userId) {
		return rewardRepository.findAllByUser_Id(userId)
				.stream().map(reward -> new UserRewardDto(
				        reward.getRewardInfo().getId(),
				        reward.getRewardInfo().getName(),
				        reward.getRewardInfo().getDescription(),
				        reward.getRewardInfo().getIconImageUrl(),
				        reward.isDefault()))
				.toList();
	}
}
