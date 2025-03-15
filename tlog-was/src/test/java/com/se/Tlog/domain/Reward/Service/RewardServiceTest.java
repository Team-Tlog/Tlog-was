package com.se.Tlog.domain.Reward.Service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.se.Tlog.domain.Reward.application.RewardService;
import com.se.Tlog.domain.Reward.domain.RewardCriteria;
import com.se.Tlog.domain.Reward.domain.RewardCriteriaType;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.infrastructure.jpa.RewardInfoRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.infrastructure.jpa.UserRepository;
import com.se.Tlog.domain.User.presentation.dto.SsoUserInfo;

import jakarta.transaction.Transactional;

@SpringBootTest
class RewardServiceTest {
	@Autowired
	private RewardService rewardService;
	@Autowired
	private RewardInfoRepository rewardInfoRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Test
	@DisplayName("개발자 유형의 보상 지급 테스트")
	void testAddRewardToUser() {
		User testUser = User.create(new SsoUserInfo("TEST_PROVIDER_ID", "TEST_EMAIL", "dev.DEVELOPER_NAME", "NULL"));
		RewardInfo testReward = RewardInfo.create("보상 1", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""));

		userRepository.save(testUser);
		rewardInfoRepository.save(testReward);
		
		assertThat(rewardService.addRewardToUser(testUser, testReward))
		.isEqualTo(true);
	}

	@Transactional
	@Test
	@DisplayName("유저가 보유한 모든 보상 조회 테스트")
	void testGetReward() {
		User testUser = User.create(new SsoUserInfo("TEST_PROVIDER_ID", "TEST_EMAIL", "dev.DEVELOPER_NAME", "NULL"));
		
		List<RewardInfo> added = new ArrayList<RewardInfo>();
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 1", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 2", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 3", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("보상 4", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		
		userRepository.save(testUser);
		
		assertThat(rewardService.addRewardToUser(testUser, added.get(0))).isEqualTo(true);
		assertThat(rewardService.addRewardToUser(testUser, added.get(1))).isEqualTo(true);
		assertThat(rewardService.addRewardToUser(testUser, added.get(2))).isEqualTo(true);
		assertThat(rewardService.addRewardToUser(testUser, added.get(3))).isEqualTo(true);
		
		assertThat(rewardService.getAllRewardOfUser(testUser.getId()))
		.containsAll(added);
	}
}
