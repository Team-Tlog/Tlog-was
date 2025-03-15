package com.se.Tlog.domain.Reward.domain.repository;

import java.util.UUID;

public interface RewardRepositoryService {
	public boolean isExist(Long rewardInfoId, UUID userId);
}
