package com.se.Tlog.domain.Notification.controller.test;

import java.util.List;

import com.se.Tlog.domain.Notification.repository.dto.FcmKeyValuePairDto;

record TestFcmMessageDto(
		List<FcmKeyValuePairDto> payloads) {
	
}

public record TestFcmMessageByTokenDto(
		List<String> tokens,
		List<TestFcmMessageDto> messages) {

}
