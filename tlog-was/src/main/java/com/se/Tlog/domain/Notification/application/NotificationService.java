package com.se.Tlog.domain.Notification.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Notification.controller.dto.AssignTokenRequestDto;
import com.se.Tlog.domain.Notification.repository.NotificationUtil;
import com.se.Tlog.domain.Notification.repository.jpa.FcmTokenRepository;
import com.se.Tlog.domain.Notification.repository.jpa.entity.UserFcmTokenJpaEntity;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@ApplicationService
public class NotificationService {
	@Autowired
	private NotificationUtil notificationUtil;
	@Autowired
	private FcmTokenRepository fcmTokenRepository;
	@Autowired
	private UserRepository userRepository;
	
	public void assignFirebaseToken(AssignTokenRequestDto request) {
		Optional<User> user = userRepository.findById(request.userId());
		if (user.isEmpty())
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		if (!notificationUtil.isValidToken(request.firebaseToken()))
			throw new CustomException(ErrorType.INVALID_FIREBASE_TOKEN);
		
		UserFcmTokenJpaEntity userFcmEntity = fcmTokenRepository.findByUserId(request.userId());
		if (userFcmEntity == null)
			userFcmEntity = new UserFcmTokenJpaEntity(user.get(), request.firebaseToken());
		else
			userFcmEntity.updateFirebaseToken(request.firebaseToken());
		fcmTokenRepository.save(userFcmEntity);
	}
}
