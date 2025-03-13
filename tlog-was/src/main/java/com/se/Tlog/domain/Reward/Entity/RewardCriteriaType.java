package com.se.Tlog.domain.Reward.Entity;

public enum RewardCriteriaType {
	/**
	 * 테스트용 보상 달성 방식
	 */
	TEST_NULL_CRITERIA,
	/**
	 * 방문 수, 리뷰 수, 팔로워 수 등
	 * <br/>사용자 관련 통계수치를 기반으로 하는 보상 달성 방식
	 */
	STATISTICAL,
	/**
	 * 개발자 전용 보상 달성 방식
	 */
	IS_DEVELOPER
}
