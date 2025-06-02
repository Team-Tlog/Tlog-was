package com.se.Tlog.domain.Team.domain;

import java.util.Random;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

public class InviteCodeUtil {
	/**
	 * 팀 초대 코드는 영문 대소문자 6자리로 주어집니다.
	 * 코드는 long 타입 정수로 변환될 수 있습니다.
	 *   각 자리는 다음의 변환 규칙을 따름 : (a:0, b:1, ... , z:25, A:26, B:27, ... , Z:51)
	 *   초대 코드를 위 규칙에 따르는 52진수로 해석하여, 코드를 정수로 변환하는 방식을 사용함.
	 */

	private static final Random random = new Random(System.currentTimeMillis());
	public static final long MAX_INVITE_CODE_COUNT = 19770609663L; // == (26 + 26) ^ 6 - 1
	public static final int INVITE_CODE_LEN = 6;
	
	public static long makeInviteCode() {
		return random.nextLong(0, MAX_INVITE_CODE_COUNT + 1);
	}
	
	public static boolean isValidCode(long inviteCode) {
		return inviteCode >= 0 && inviteCode <= MAX_INVITE_CODE_COUNT;
	}
	
	public static long strToLong(String inviteCode) {
		if (null == inviteCode || inviteCode.length() != INVITE_CODE_LEN)
			throw new CustomException(ErrorType.INTERNAL_ERROR_BY_INVITE_CODE);
		
		long codeValue = 0;
		long multiply = 1;
		for (int i = INVITE_CODE_LEN - 1; i >= 0; i--) {
			char code = inviteCode.charAt(i);
			if (code >= 'a' && code <= 'z') {
				codeValue += multiply * (code - 'a');
			}
			else if (code >= 'A' && code <= 'Z') {
				codeValue += multiply * (26 + (code - 'A'));
			}
			else {
				throw new CustomException(ErrorType.INTERNAL_ERROR_BY_INVITE_CODE);
			}
			multiply *= 52;
		}
		
		return codeValue;
	}
	
	public static String longToStr(long inviteCode) {
		if (!isValidCode(inviteCode))
			throw new CustomException(ErrorType.INTERNAL_ERROR_BY_INVITE_CODE);
		
		char[] codes = new char[INVITE_CODE_LEN];
		for (int i = INVITE_CODE_LEN - 1; i >= 0; i--) {
			int code = (int)(inviteCode % (2 * 26));
			if (code < 26) {
				codes[i] = (char)('a' + code);
			} else {
				code -= 26;
				codes[i] = (char)('A' + code);
			}
			inviteCode /= 52;
		}
		
		return new String(codes);
	}
}
