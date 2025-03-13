package com.se.Tlog.domain.Reward.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.Reward.Entity.Reward;
import com.se.Tlog.domain.Reward.Entity.RewardInfo;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;
import com.se.Tlog.domain.Reward.repository.jpa.RewardRepository;
import com.se.Tlog.domain.User.Entity.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
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
			throw new IllegalArgumentException("존재하지 않는 사용자 또는 보상 형식");
		}
	}
	
	/**
	 * 특정 유저가 보유하고 있는 모든 보상을 조회합니다.
	 * @param user
	 * @return
	 */
	public List<RewardInfo> getAllRewardOfUser(User user) {
		return getAllRewardOfUser(user.getId());
	}
	
	/**
	 * 특정 유저가 보유하고 있는 모든 보상을 조회합니다.
	 * @param userId
	 * @return
	 */
	public List<RewardInfo> getAllRewardOfUser(UUID userId) {
		return rewardRepository.findAllByUser_Id(userId)
				.stream().map((reward)-> {return reward.getRewardInfo();})
				.toList();
	}
}
