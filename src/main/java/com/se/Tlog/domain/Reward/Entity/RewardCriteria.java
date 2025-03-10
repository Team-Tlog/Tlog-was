package com.se.Tlog.domain.Reward.Entity;

import com.se.Tlog.domain.User.Entity.User;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 보상 달성 조건을 나타내는 엔티티
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardCriteria {
	/**
	 * 검사 방식
	 */
	private RewardCriteriaType criteriaType;
	
	/**
	 * 검사 방식에 관한 구체적인 규칙
	 * <br/> 예를 들어, 통계 방식의 규칙이 설정되었다면, <code>"ExampleValue>=100"</code>과 같은 세부 규칙을 설정합니다.
	 */
	private String criteriaParameter;
	
	public RewardCriteria(RewardCriteriaType type, String parameter) {
		if (!isAvailCriteria(type, parameter))
			throw new IllegalArgumentException("사용할 수 없는 조건입니다!");
		this.criteriaType = type;
		this.criteriaParameter = parameter;
	}

	/**
	 * 보상 조건이 사용 가능한지 검사합니다.
	 * @param criteria
	 * @return
	 */
	public static boolean isAvailCriteria(RewardCriteria criteria) {
		return isAvailCriteria(criteria);
	}
	
	/**
	 * 보상 조건이 사용 가능한지 검사합니다.
	 * @param type
	 * @param parameter
	 * @return
	 */
	public static boolean isAvailCriteria(RewardCriteriaType type, String parameter) {
		switch (type) {
			case TEST_NULL_CRITERIA:
				return isAvailTestNullCriteria();
			case STATISTICAL:
				return isAvailStatistical(parameter);
			case IS_DEVELOPER:
				return isAvailIsDeveloper();
			default:
				return false;
		}
	}
	
	private static boolean isAvailTestNullCriteria() {
		return true;
	}
	
	private static boolean isAvailStatistical(String parameter) {
		// parameter 유효성 검사 로직이 구현될 것.
		return true;
	}
	
	private static boolean isAvailIsDeveloper() {
		return true;
	}
	
	/**
	 * 특정 유저가 조건을 만족하는지 검사합니다.
	 * @param user
	 * @exception CustomException 보상이 지원되지 않는 기준을 가지면 발생합니다.
	 */
	public boolean meetsCriteria(User user) {
		return meetsCriteria(user, this);
	}
	
	/**
	 * 특정 유저가 조건을 만족하는지 검사합니다.
	 * @param user
	 * @exception CustomException 보상이 지원되지 않는 기준을 가지면 발생합니다.
	 */
	public static boolean meetsCriteria(User user, RewardCriteria criteria) {
		switch (criteria.criteriaType) {
			case TEST_NULL_CRITERIA:
				return testNullCriteria();
			case STATISTICAL:
				return statistical(user, criteria.criteriaParameter);
			case IS_DEVELOPER:
				return isDeveloper(user);
			default:
				throw new CustomException(ErrorType.UNSUPPORTED_REWARD_CRITERIA);
		}
	}

	private static boolean testNullCriteria() {
		return true;
	}
	
	private static boolean statistical(User user, String criteriaParameter) {
		// 실제 사용자 통계를 불러오는 로직이 구현될 것.
		return true;
	}
	
	private static boolean isDeveloper(User user) {
		if (user.getName() != null)
			return user.getName().startsWith("dev");
		return false;
	}
}
