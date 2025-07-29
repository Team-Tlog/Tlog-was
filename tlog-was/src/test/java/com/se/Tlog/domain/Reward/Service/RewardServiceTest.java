package com.se.Tlog.domain.Reward.Service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.se.Tlog.domain.Reward.application.RewardService;
import com.se.Tlog.domain.Reward.controller.dto.UserRewardDto;
import com.se.Tlog.domain.Reward.domain.RewardCriteria;
import com.se.Tlog.domain.Reward.domain.RewardCriteriaType;
import com.se.Tlog.domain.Reward.domain.RewardInfo;
import com.se.Tlog.domain.Reward.repository.jpa.RewardInfoRepository;
import com.se.Tlog.domain.User.controller.dto.RegisterUserProfileDto;
import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.UserRegisterInfo;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;

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
		User testUser = User.create(
		        new UserRegisterInfo(
		                new SsoUserInfo("TEST_PROVIDER_ID", "TEST_EMAIL", "dev.DEVELOPER_NAME", "NULL"),
		                new RegisterUserProfileDto("00000000", new ArrayList<>())));
		RewardInfo testReward = RewardInfo.create("", "보상 1", "테스트 보상 1", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""));

		userRepository.save(testUser);
		rewardInfoRepository.save(testReward);
		
		assertThatNoException().isThrownBy(() -> rewardService.addRewardToUser(testUser.getId(), testReward.getId()));
	}

	@Transactional
	@Test
	@DisplayName("유저가 보유한 모든 보상 조회 테스트")
	void testGetReward() {
		User testUser = User.create(
		        new UserRegisterInfo(
		                new SsoUserInfo("TEST_PROVIDER_ID", "TEST_EMAIL", "dev.DEVELOPER_NAME", "NULL"), 
		                new RegisterUserProfileDto("00000000", new ArrayList<>())));
		
		List<RewardInfo> added = new ArrayList<RewardInfo>();
		added.add(rewardInfoRepository.save(RewardInfo.create("", "보상 1", "테스트 보상 1", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("", "보상 2", "테스트 보상 2", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("", "보상 3", "테스트 보상 3", RewardCriteria.create(RewardCriteriaType.TEST_NULL_CRITERIA, ""))));
		added.add(rewardInfoRepository.save(RewardInfo.create("", "보상 4", "테스트 보상 4", RewardCriteria.create(RewardCriteriaType.IS_DEVELOPER, ""))));
		
		userRepository.save(testUser);
		
		assertThatNoException().isThrownBy(()->rewardService.addRewardToUser(testUser.getId(), added.get(0).getId()));
		assertThatNoException().isThrownBy(()->rewardService.addRewardToUser(testUser.getId(), added.get(1).getId()));
		assertThatNoException().isThrownBy(()->rewardService.addRewardToUser(testUser.getId(), added.get(2).getId()));
		assertThatNoException().isThrownBy(()->rewardService.addRewardToUser(testUser.getId(), added.get(3).getId()));
		
		List<UserRewardDto> result = rewardService.getAllRewardOfUser(testUser.getId());
		assertEquals(result.size(), added.size());
		for (int i = 0; i < result.size(); i++) {
		    assertEquals(result.get(i).rewardId(), added.get(i).getId());
		    assertEquals(result.get(i).name(), added.get(i).getName());
		}
	}
}
