package com.se.Tlog.domain.Reward.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.Reward.Entity.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {
	List<Reward> findAllByUser_Id(UUID id);
	boolean existsByRewardInfo_IdAndUser_Id(Long rewardInfoId, UUID userId);
}
