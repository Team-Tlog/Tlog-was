package com.se.Tlog.domain.Reward.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.Reward.Entity.RewardCriteria;
import com.se.Tlog.domain.Reward.Entity.RewardCriteriaType;
import com.se.Tlog.domain.Reward.Entity.RewardInfo;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardInfoService {
	private final RewardInfoRepository rewardInfoRepository;
	
	/**
	 * 특정 보상 형식을 등록합니다.
	 * <br/> 보상 달성 조건이 올바르지 않은 형식이면 새 보상 형식 생성에 실패하며, <code>null</code>을 반환합니다.
	 * @param type
	 * @param parameter
	 * @return
	 */
	public RewardInfo addNewRewardInfo(String name, RewardCriteriaType type, String parameter) {
		RewardCriteria criteria = null;
		try {
			criteria = RewardCriteria.create(type, parameter);
		} catch (Exception e) {
			return null;
		}
		
		return rewardInfoRepository.save(RewardInfo.create(name, criteria));
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
