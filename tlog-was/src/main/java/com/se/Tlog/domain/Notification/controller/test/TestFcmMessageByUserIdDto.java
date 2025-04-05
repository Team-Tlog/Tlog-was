package com.se.Tlog.domain.Notification.controller.test;

import java.util.List;
import java.util.UUID;

public record TestFcmMessageByUserIdDto(
		List<UUID> users,
		List<TestFcmMessageDto> messages) {

}
