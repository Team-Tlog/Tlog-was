package com.se.Tlog.domain.Notification.controller.test;

import java.util.List;
import java.util.Map;

public record TestNotificationDto(
        List<String> tokens,
        Map<String, String> data) {

}
