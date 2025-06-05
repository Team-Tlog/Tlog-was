package com.se.Tlog.domain.Reward.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Reward.controller.dto.UserRewardDto;
import com.se.Tlog.domain.Reward.domain.Reward;
import com.se.Tlog.domain.Reward.domain.repository.RewardRepositoryService;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;
import com.se.Tlog.domain.Reward.repository.jpa.RewardRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class RewardService {
	private final RewardRepositoryService rewardRepo;
	private final RewardRepository rewardRepository;
	private final RewardInfoRepository rewardInfoRepository;
	private final UserRepository userRepository;
	
	/**
	 * 특정 유저에게 보상을 설정합니다.
	 * <br/> 보상 조건이 맞지 않으면 보상 설정에 실패하며, <code>false</code>를 반환합니다.
	 * @param userId
	 * @param rewardInfoId
	 * @return
	 */
	public void addRewardToUser(UUID userId, Long rewardInfoId) {
		try {
			rewardRepository.save(
					Reward.create(
							userRepository.findById(userId).get(),
							rewardInfoRepository.findById(rewardInfoId).get(), 
							rewardRepo));
		} catch (NoSuchElementException e) {
			throw new CustomException(ErrorType.NOT_FOUND);
		}
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
				        reward.getRewardInfo().getIconImageUrl()))
				.toList();
	}
}
