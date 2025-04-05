package com.se.Tlog.domain.Notification.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Notification.repository.dto.FcmMessageDto;
import com.se.Tlog.domain.Notification.repository.dto.FcmRequestDto;
import com.se.Tlog.domain.Notification.repository.jpa.FcmTokenRepository;
import com.se.Tlog.domain.Notification.repository.jpa.entity.UserFcmTokenJpaEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
public class NotificationUtil {
	@Autowired
	private FcmWrapper fcmWrapper;
	@Autowired
	private FcmTokenRepository fcmTokenRepository;
	
	public boolean isValidToken(String firebaseToken) {
		return fcmWrapper.isValidToken(firebaseToken);
	}
	
	/**
	 * 단일 메세지를 전송합니다.
	 * <br/> 알림을 전송할 유저의 클라이언트가 등록되지 않은 경우, <code>false</code>를 반환합니다.
	 * <br/> 여러 메세지를 전송할 경우, <code>sendNotifications()</code>를 사용할 것을 권장합니다.
	 * @param request
	 */
	public boolean sendNotification(FcmRequestDto request) {
		UserFcmTokenJpaEntity dbEntity = fcmTokenRepository.findByUserId(request.userId());
		if (dbEntity == null)
			return false;
		
		fcmWrapper.sendFcmMessage(new FcmMessageDto(dbEntity.getFcmToken(), request.payload()));
		return true;
	}
	
	/**
	 * 동일 메세지를 여러 클라이언트에 전송합니다.
	 * <br/> 등록된 클라이언트가 있어 요청을 진행한 수를 반환합니다. 
	 * @param users
	 * @param sendPayload <code>sendPayload.userId</code>는 사용되지 않습니다.
	 */
	public int sendNotifications(List<UUID> users, FcmRequestDto request) {
		// DB 접근의 최소화를 위해, user -> token 일괄 변환
		List<String> tokens = fcmTokenRepository.findFcmTokenByUserIdIn(users);
		int validTokens = tokens.size();
		
		if (validTokens > 0)
			fcmWrapper.sendFcmMessages(tokens, new FcmMessageDto(null, request.payload()));
		return validTokens;
	}
	
	/**
	 * 서로 다른 여러 메세지를 여러 클라이언트에 전송합니다.
	 * <br/> 등록된 클라이언트가 있어 요청을 진행한 수를 반환합니다.
	 * @param sendData
	 */
	public int sendNotifications(List<FcmRequestDto> requests) {
		// DB 접근의 최소화를 위해, user -> token 일괄 변환
		List<UUID> users = new ArrayList<UUID>();
		for (FcmRequestDto m : requests)
			users.add(m.userId());
		if (users.size() == 0)
			return 0;
		
		Map<UUID, String> tokenMap = fcmTokenRepository.findAllByUserIdIn(users)
				.stream().collect(Collectors.toMap(e -> e.getUser().getId(), e -> e.getFcmToken()));
		if (tokenMap.size() == 0)
			return 0;

		List<FcmMessageDto> messages = new ArrayList<FcmMessageDto>();
		for (FcmRequestDto m : requests)
			if (tokenMap.containsKey(m.userId()))
				messages.add(new FcmMessageDto(tokenMap.get(m.userId()), m.payload()));
		fcmWrapper.sendFcmMessages(messages);
		
		return tokenMap.size();
	}
}
