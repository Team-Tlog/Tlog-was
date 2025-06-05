package com.se.Tlog.domain.Reward.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

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
    
    private String iconImageUrl;
	
	private String name;
    
    private String description;
	
	@Embedded
	private RewardCriteria rewardCriteria;
	
	private RewardInfo(String iconImageUrl, String name, String description, RewardCriteria rewardCriteria) {
	    this.iconImageUrl = iconImageUrl;
		this.name = name;
		this.description = description;
		this.rewardCriteria = rewardCriteria;
	}
	
	public static RewardInfo create(String iconImageUrl, String name, String description, RewardCriteria rewardCriteria) {
	    if (iconImageUrl == null
	            || name == null || name.trim().isEmpty()
	            || description == null || description.trim().isEmpty()
	            || rewardCriteria == null)
	        throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
	    
		return new RewardInfo(iconImageUrl, name, description, rewardCriteria);
	}
}
