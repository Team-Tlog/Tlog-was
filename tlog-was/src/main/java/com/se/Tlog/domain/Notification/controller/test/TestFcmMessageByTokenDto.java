package com.se.Tlog.domain.Notification.controller.test;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

record TestFcmMessageDto(
        @Schema(example = "{\"key1\": \"value1\", \"key2\": \"value2\"}")
		Map<String, String> payloads) {
	
}

public record TestFcmMessageByTokenDto(
		List<String> tokens,
		List<TestFcmMessageDto> messages) {

}
