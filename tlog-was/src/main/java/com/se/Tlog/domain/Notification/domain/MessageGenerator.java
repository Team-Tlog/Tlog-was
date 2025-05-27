package com.se.Tlog.domain.Notification.domain;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Notification.repository.dto.FcmRequestDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageGenerator {
    private static final String KEYWORD_MESSAGE_TYPE = "tlog-message-type";
    private static final String KEYWORD_LINK_TYPE = "link-type";
    private static final String KEYWORD_LINK_ADDRESS = "link-address";
    private static final String KEYWORD_CONTENT = "content";
    private static final String KEYWORD_ACTOR_ID = "actor-id";
    private static final String KEYWORD_ACTOR_IMAGE = "actor-image";
    private static final String KEYWORD_OBJECT_ID = "object-id";
    private static final String KEYWORD_OBJECT_IMAGE = "object-image";
    private static final String KEYWORD_FOLLOW_STATE = "is-following";
    
    // FCM 메시지 규격이 맞는지 확인하는 역할이므로
    // receiverId가 굳이 필요없다 판단되면
    // 추후 삭제할 예정입니다.
    
    public FcmRequestDto getMessage(UUID receiverId, Map<String, String> payload) {
        try {
            return 
                    switch (MessageType.of(payload.get(KEYWORD_MESSAGE_TYPE))) {
                    case DEFAULT_STRING_MESSAGE -> getDefaultStringMessage(
                            receiverId, 
                            payload.get(KEYWORD_CONTENT));
                    case LINKABLE_MESSAGE -> getLinkableMessage(
                            receiverId, 
                            LinkType.of(payload.get(KEYWORD_LINK_TYPE)),
                            payload.get(KEYWORD_LINK_ADDRESS),
                            payload.get(KEYWORD_CONTENT));
                    case DEFAULT_SNS_MESSAGE -> getDefaultSnsMessage(
                            receiverId,
                            UUID.fromString(payload.get(KEYWORD_ACTOR_ID)),
                            payload.get(KEYWORD_ACTOR_IMAGE),
                            payload.get(KEYWORD_CONTENT),
                            payload.get(KEYWORD_OBJECT_ID),
                            payload.get(KEYWORD_OBJECT_IMAGE));
                    case FOLLOW_MESSAGE -> getFollowMessage(
                            receiverId, 
                            UUID.fromString(payload.get(KEYWORD_ACTOR_ID)),
                            payload.get(KEYWORD_ACTOR_IMAGE),
                            payload.get(KEYWORD_CONTENT),
                            Boolean.getBoolean(payload.get(KEYWORD_FOLLOW_STATE)));
                    };
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorType.INVALID_TLOG_MESSAGE);
        }
    }
    
    public FcmRequestDto getDefaultStringMessage(
            UUID receiverId,
            String content) {
        return new FcmRequestDto(
                receiverId, 
                Map.of(
                        KEYWORD_MESSAGE_TYPE, MessageType.DEFAULT_STRING_MESSAGE.code(),
                        KEYWORD_CONTENT, content));
    }
    
    public FcmRequestDto getLinkableMessage(
            UUID receiverId,
            LinkType linkType,
            String linkAddress,
            String content) {
        return new FcmRequestDto(
                receiverId, 
                Map.of(
                        KEYWORD_MESSAGE_TYPE, MessageType.LINKABLE_MESSAGE.code(),
                        KEYWORD_LINK_TYPE, linkType.code(),
                        KEYWORD_LINK_ADDRESS, linkAddress,
                        KEYWORD_CONTENT, content));
    }
    
    public FcmRequestDto getDefaultSnsMessage(
            UUID receiverId, 
            UUID actorId, 
            String actorImage,
            String content,
            String objectId, // 게시물 id의 형식에 맞추어 변경 예정
            String objectImage) {
        return new FcmRequestDto(
                receiverId, 
                Map.of(
                        KEYWORD_MESSAGE_TYPE, MessageType.DEFAULT_SNS_MESSAGE.code(),
                        KEYWORD_ACTOR_ID, actorId.toString(),
                        KEYWORD_ACTOR_IMAGE, actorImage,
                        KEYWORD_CONTENT, content,
                        KEYWORD_OBJECT_ID, objectId,
                        KEYWORD_OBJECT_IMAGE, objectImage));
    }
    
    public FcmRequestDto getFollowMessage(
            UUID receiverId, 
            UUID actorId, 
            String actorImage,
            String content,
            boolean isFollowing) {
        return new FcmRequestDto(
                receiverId, 
                Map.of(
                        KEYWORD_MESSAGE_TYPE, MessageType.FOLLOW_MESSAGE.code(),
                        KEYWORD_ACTOR_ID, actorId.toString(),
                        KEYWORD_ACTOR_IMAGE, actorImage,
                        KEYWORD_CONTENT, content,
                        KEYWORD_FOLLOW_STATE, Boolean.toString(isFollowing)));
    }
}
