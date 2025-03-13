package com.se.Tlog.domain.Reward.Entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서비스에 존재하는 보상 정보를 나타내는 엔티티입니다. 
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Embedded
	private RewardCriteria rewardCriteria;
	
	// private media icon // 보상 뱃지 아이콘 등 부가 정보
	
	// private String description = "팔로워 수를 100명 돌파했어요!!"
	
	private RewardInfo(String name, RewardCriteria rewardCriteria) {
		this.name = name;
		this.rewardCriteria = rewardCriteria;
	}
	
	public static RewardInfo create(String name, RewardCriteria rewardCriteria) {
		return new RewardInfo(name, rewardCriteria);
	}
}
