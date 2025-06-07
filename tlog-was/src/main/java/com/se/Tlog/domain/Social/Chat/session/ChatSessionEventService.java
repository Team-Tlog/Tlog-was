package com.se.Tlog.domain.Social.Chat.session;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.session.repository.jpa.ConnectedUserSessionRepository;
import com.se.Tlog.domain.Social.Chat.read.ChatReadStatusService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionEventService {  // 명령어별 처리 로직
    private final ChatReadStatusService chatReadStatusService;
    private final ChatMessageRepository chatMessageRepository;
    private final ConnectedUserSessionRepository connectedUserSessionRepository;
    private final ConnectedUserSessionService connectedUserSessionService;

    public void handleConnect(StompHeaderAccessor accessor) {
        Principal principal = (Principal) accessor.getSessionAttributes().get("userPrincipal");
        accessor.setUser(principal);
        log.info("사용자 연결: {}", principal.getName());
    }

    public void handleSubscribe(StompHeaderAccessor accessor) {
        //구독 경로 "/sub/chat/room/${roomId}"
        System.out.println("accessor.getUser() = " + accessor.getUser());
        Principal user = accessor.getUser();
        String sessionId = accessor.getSessionId();

        String destination = accessor.getDestination();
        if (destination == null) {
            throw new CustomException(ErrorType.INVALID_DESTINATION);
        }
        Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));

        connectedUserSessionService.connect(sessionId,UUID.fromString(user.getName()),roomId);
    }

    public void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Principal principal = accessor.getUser();
        UUID userId = UUID.fromString(principal.getName());

        Optional<ConnectedUserSession> optionalSession = connectedUserSessionRepository.findById(sessionId);
        if (optionalSession.isEmpty()) {
            log.warn("DISCONNECT 처리 중 : 이미 세션이 종료되었거나 존재하지 않습니다. sessionId={}", sessionId);
            return;
        }
        ConnectedUserSession connectedUserSession = optionalSession.get();
        ChatMessage roomLastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByIdDesc(connectedUserSession.getRoomId());
        if (roomLastMessage != null) {
            chatReadStatusService.updateReadStatus(userId, connectedUserSession.getRoomId(), roomLastMessage.getId());
        }else {
            log.warn("DISCONNECT 처리 중 : 채팅방에 메시지가 없습니다. rooId={}", connectedUserSession.getRoomId());
        }
        connectedUserSessionService.disconnect(sessionId);
    }
}
