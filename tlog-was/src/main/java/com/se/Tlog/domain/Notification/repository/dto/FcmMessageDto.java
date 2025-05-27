package com.se.Tlog.domain.Notification.repository.dto;

import java.util.Map;

public record FcmMessageDto(
		String fcmToken,
		Map<String, String> payload) {

}
