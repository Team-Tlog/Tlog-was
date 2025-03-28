package com.se.Tlog.domain.Team.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class InviteCodeUtilTest {
	
	@Test
	@DisplayName("팀 초대코드 변환 테스트")
	void codeConversionTest() {
		// min
		assertThat(InviteCodeUtil.longToStr(0)).isEqualTo("aaaaaa");
		assertThat(InviteCodeUtil.strToLong("aaaaaa")).isEqualTo(0);
		
		// max
		assertThat(InviteCodeUtil.longToStr(19770609663L)).isEqualTo("ZZZZZZ");
		assertThat(InviteCodeUtil.strToLong("ZZZZZZ")).isEqualTo(19770609663L);
		
		assertThat(InviteCodeUtil.longToStr(5)).isEqualTo("aaaaaf");
		assertThat(InviteCodeUtil.strToLong("aaaaaf")).isEqualTo(5);
		
		assertThat(InviteCodeUtil.longToStr(12315125)).isEqualTo("abJEvr");
		assertThat(InviteCodeUtil.strToLong("abJEvr")).isEqualTo(12315125);
		
		// over range
		assertThatException().isThrownBy(() -> InviteCodeUtil.longToStr(-1));
		assertThatException().isThrownBy(() -> InviteCodeUtil.longToStr(19770609664L));
		assertThatException().isThrownBy(() -> InviteCodeUtil.longToStr(12513136742742L));
		assertThatException().isThrownBy(() -> InviteCodeUtil.longToStr(-12513136742742L));
	}
	
	@Test
	@DisplayName("팀 초대코드 생성 및 변환 테스트")
	void codeGenerateTest() {
		for (int round = 0; round < 500; round++) {
			long code = InviteCodeUtil.makeInviteCode();
			String codeStr = InviteCodeUtil.longToStr(code);
			assertThat(InviteCodeUtil.strToLong(codeStr)).isEqualTo(code);	
		}
	}
}
