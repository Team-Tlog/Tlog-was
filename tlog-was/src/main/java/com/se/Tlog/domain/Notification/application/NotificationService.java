package com.se.Tlog.domain.Notification.application;

import java.util.Optional;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Notification.controller.dto.AssignTokenRequestDto;
import com.se.Tlog.domain.Notification.domain.LinkType;
import com.se.Tlog.domain.Notification.domain.MessageGenerator;
import com.se.Tlog.domain.Notification.repository.NotificationUtil;
import com.se.Tlog.domain.Notification.repository.jpa.FcmTokenRepository;
import com.se.Tlog.domain.Notification.repository.jpa.entity.UserFcmTokenJpaEntity;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;


/** =====================================
 * 
 *   알림 전송 실패시 기본 정책 :
 * 
 *      모든 알림은 전송 실패시 무시하고 계속 진행합니다.
 *
 *  ===================================== */

@ApplicationService
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationUtil notificationUtil;
	private final FcmTokenRepository fcmTokenRepository;
	private final UserRepository userRepository;
	private final MessageGenerator messageGenerator;
	
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

	public void sendDefaultStringMessage(UUID receiverId, String content) {
        notificationUtil.sendNotification(
                messageGenerator.getDefaultStringMessage(receiverId, content));
    }
    
    public void sendMyPageLinkMessage(
            UUID receiverId,
            String content) {
        notificationUtil.sendNotification(
                messageGenerator.getLinkableMessage(
                        receiverId,
                        LinkType.PAGE_MYPAGE,
                        "",
                        content));
    }
	
	public void sendDestinationLinkMessage(
            UUID receiverId,
            String destinationId,
            String content) {
        notificationUtil.sendNotification(
                messageGenerator.getLinkableMessage(
                        receiverId,
                        LinkType.LINK_DESTINATION,
                        destinationId,
                        content));
    }
    
    public void sendUserLinkMessage(
            UUID receiverId,
            UUID linkedUserId,
            String content) {
        notificationUtil.sendNotification(
                messageGenerator.getLinkableMessage(
                        receiverId,
                        LinkType.LINK_DESTINATION,
                        linkedUserId.toString(),
                        content));
    }
	
	public void sendDefaultSnsMessage(
	        UUID receiverId, 
            UUID actorId, 
            String actorImage,
            String content,
            String objectId, // 게시물 id의 형식에 맞추어 변경 예정
            String objectImage) {
        notificationUtil.sendNotification(
                messageGenerator.getDefaultSnsMessage(
                        receiverId,
                        actorId,
                        actorImage,
                        content,
                        objectId,
                        objectImage));
    }
	
	public void sendFollowMessage(
            UUID receiverId, 
            UUID actorId, 
            String actorImage,
            String content,
            boolean isFollowing) {
        notificationUtil.sendNotification(
                messageGenerator.getFollowMessage(
                        receiverId,
                        actorId,
                        actorImage,
                        content,
                        isFollowing));
    }
}
