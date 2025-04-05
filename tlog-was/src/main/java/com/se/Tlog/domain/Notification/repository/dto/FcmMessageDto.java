package com.se.Tlog.domain.Notification.repository.dto;

import java.util.List;

public record FcmMessageDto(
		String fcmToken,
		List<FcmKeyValuePairDto> payload) {

}
