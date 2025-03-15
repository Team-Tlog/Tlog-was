package com.se.Tlog.domain.Reward.Service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.se.Tlog.domain.Reward.application.RewardInfoService;
import com.se.Tlog.domain.Reward.domain.RewardCriteria;
import com.se.Tlog.domain.Reward.domain.RewardCriteriaType;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.infrastructure.jpa.RewardInfoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class RewardInfoServiceTest {
	@Autowired
	private RewardInfoService rewardInfoService;
	@Autowired
	private RewardInfoRepository rewardInfoRepository;
	
	@Transactional
	@Test
	@DisplayName("새 보상 형식 등록 테스트")
	void testAddNewRewardInfo() {
		List<RewardInfo> added = new ArrayList<RewardInfo>();
		added.add(rewardInfoService.addNewRewardInfo("보상 1", RewardCriteriaType.TEST_NULL_CRITERIA, ""));
		added.add(rewardInfoService.addNewRewardInfo("보상 2", RewardCriteriaType.IS_DEVELOPER, ""));
		added.add(rewardInfoService.addNewRewardInfo("보상 3", RewardCriteriaType.TEST_NULL_CRITERIA, ""));
		added.add(rewardInfoService.addNewRewardInfo("보상 4", RewardCriteriaType.IS_DEVELOPER, ""));
		
		assertThat(rewardInfoRepository.findAll())
		.containsAll(added);
	}

	@Transactional
	@Test
	@DisplayName("보상 형식 조회 테스트")
	void testGetAllRewardInfo() {
		List<RewardInfo> added = new ArrayList<RewardInfo>();
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 1", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 2", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 3", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 4", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		
		assertThat(rewardInfoService.getAllRewardInfo())
		.containsAll(added);
	}
	
	@Transactional
	@Test
	@DisplayName("보상 형식 제거 테스트")
	void testDeleteRewardInfo() {
		List<RewardInfo> added = new ArrayList<RewardInfo>();
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 1", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 2", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 3", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 4", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		
		rewardInfoService.deleteRewardInfo(added.get(1).getId());
		rewardInfoService.deleteRewardInfo(added.get(3).getId());
		
		assertThat(rewardInfoRepository.findAll())
		.contains(
				added.get(0), 
				added.get(2))
		.doesNotContain(
				added.get(1),
				added.get(3));
	}
}
