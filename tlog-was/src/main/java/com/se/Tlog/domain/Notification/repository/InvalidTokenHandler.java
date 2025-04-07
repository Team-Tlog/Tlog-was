package com.se.Tlog.domain.Notification.repository;

import org.springframework.stereotype.Component;

@Component
public class InvalidTokenHandler {
	public void handle(String token) {
		// 유효하지 않은 FCM 토큰 처리 로직 필요
	}
}
