package com.se.Tlog.domain.Reward.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Reward.domain.RewardInfo;

@Repository
public interface RewardInfoRepository extends JpaRepository<RewardInfo, Long> {

}
