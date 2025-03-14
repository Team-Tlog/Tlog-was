package com.se.Tlog.domain.Reward.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.Reward.domain.RewardInfo;

public interface RewardInfoRepository extends JpaRepository<RewardInfo, Long> {

}
