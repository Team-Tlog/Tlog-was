package com.se.Tlog.domain.Notification.controller.dto;

import java.util.UUID;

public record AssignTokenRequestDto(
		UUID userId,
		String firebaseToken) {

}
