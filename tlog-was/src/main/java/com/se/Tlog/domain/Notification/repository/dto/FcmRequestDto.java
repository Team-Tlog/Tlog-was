package com.se.Tlog.domain.Notification.repository.dto;

import java.util.List;
import java.util.UUID;

public record FcmRequestDto(
		UUID userId,
		List<FcmKeyValuePairDto> payload) {

}
