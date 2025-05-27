package com.se.Tlog.domain.Notification.repository.dto;

import java.util.Map;
import java.util.UUID;

public record FcmRequestDto(
		UUID userId,
		Map<String, String> payload) {

}
