package com.se.Tlog.domain.Reward.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Reward.domain.Reward;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.infrastructure.jpa.RewardInfoRepository;
import com.se.Tlog.domain.Reward.infrastructure.jpa.RewardRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.infrastructure.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class RewardService {
	private final RewardRepository rewardRepository;
	private final RewardInfoRepository rewardInfoRepository;
	private final UserRepository userRepository;
	
	/**
	 * 특정 유저에게 보상을 설정합니다.
	 * <br/> 보상 조건이 맞지 않으면 보상 설정에 실패하며, <code>false</code>를 반환합니다.
	 * @param user
	 * @param reward
	 * @return
	 */
	public boolean addRewardToUser(User user, RewardInfo reward) {
		if (!reward.getRewardCriteria().meetsCriteria(user))
			return false;
		
		if (!rewardRepository.existsByRewardInfo_IdAndUser_Id(reward.getId(), user.getId()))
			rewardRepository.save(Reward.create(user, reward));
		return true;
	}
	
	/**
	 * 특정 유저에게 보상을 설정합니다.
	 * <br/> 보상 조건이 맞지 않으면 보상 설정에 실패하며, <code>false</code>를 반환합니다.
	 * @param userId
	 * @param rewardInfoId
	 * @return
	 */
	public boolean addRewardToUser(UUID userId, Long rewardInfoId) {
		try {
			return addRewardToUser(
					userRepository.findById(userId).get(), 
					rewardInfoRepository.findById(rewardInfoId).get());
		} catch (NoSuchElementException e) {
			throw new CustomException(ErrorType.NOT_FOUND);
		}
	}
	
	/**
	 * 특정 유저가 보유하고 있는 모든 보상을 조회합니다.
	 * @param userId
	 * @return
	 */
	public List<RewardInfo> getAllRewardOfUser(UUID userId) {
		return rewardRepository.findAllByUser_Id(userId)
				.stream().map(Reward::getRewardInfo)
				.toList();
	}
}
