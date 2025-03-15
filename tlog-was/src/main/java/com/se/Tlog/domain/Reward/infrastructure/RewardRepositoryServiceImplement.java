package com.se.Tlog.domain.Reward.infrastructure;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Reward.domain.repository.RewardRepositoryService;
import com.se.Tlog.domain.Reward.infrastructure.jpa.RewardRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardRepositoryServiceImplement implements RewardRepositoryService {
	private final RewardRepository rewardRepository; 

	@Override
	public boolean isExist(Long rewardInfoId, UUID userId) {
		return rewardRepository.existsByRewardInfo_IdAndUser_Id(rewardInfoId, userId);
	}

}
