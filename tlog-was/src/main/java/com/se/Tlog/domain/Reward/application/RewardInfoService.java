package com.se.Tlog.domain.Reward.application;

import java.util.List;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Reward.controller.dto.CreateRewardInfoRequest;
import com.se.Tlog.domain.Reward.domain.RewardCriteria;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class RewardInfoService {
	private final RewardInfoRepository rewardInfoRepository;
	
	/**
	 * 특정 보상 형식을 등록합니다.
	 * @param name
	 * @param type
	 * @param parameter
	 * @return
	 */
	public RewardInfo addNewRewardInfo(CreateRewardInfoRequest request) {
		RewardInfo newRewardInfo = 
				RewardInfo.create(
						request.name(),
						RewardCriteria.create(request.criteriaType(), request.criteriaParameter()));
		return rewardInfoRepository.save(newRewardInfo);
	}

	/**
	 * 현재 서비스에 존재하는 모든 보상 형식을 반환합니다.
	 * @return
	 */
	public List<RewardInfo> getAllRewardInfo() {
		return rewardInfoRepository.findAll();
	}
	
	/**
	 * 특정 보상 형식을 제거합니다.
	 * @param id
	 */
	public void deleteRewardInfo(Long id) {
		rewardInfoRepository.deleteById(id);
	}
}
