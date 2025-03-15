package com.se.Tlog.domain.Reward.domain;

import com.se.Tlog.domain.Reward.domain.repository.RewardRepositoryService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저별 보상 획득 기록을 나타내는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "reward_info")
	private RewardInfo rewardInfo;
	
	private Reward(User user, RewardInfo rewardInfo) {
		this.user = user;
		this.rewardInfo = rewardInfo;
	}
	
	public static Reward create(User user, RewardInfo rewardInfo, RewardRepositoryService rewardRepo) {
		if (!rewardInfo.getRewardCriteria().meetsCriteria(user))
			throw new CustomException(ErrorType.NOT_FIT_ON_CRITERIA);
		if (rewardRepo.isExist(rewardInfo.getId(), user.getId()))
			throw new CustomException(ErrorType.ALREADY_OWN_REWARD);
		return new Reward(user, rewardInfo);
	}
}
