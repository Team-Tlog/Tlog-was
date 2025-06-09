package com.se.Tlog.global.security.handler;

import com.se.Tlog.domain.Social.Chat.session.ChatSessionEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final ChatSessionEventService chatSessionEventService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("message = " + message);
        System.out.println("accessor = " + accessor);
        Principal principal = (Principal) accessor.getSessionAttributes().get("userPrincipal");
        accessor.setUser(principal);

        Map<String, List<String>> nativeHeaderMap = accessor.toNativeHeaderMap();
        nativeHeaderMap.forEach((key,values) ->{
            System.out.println("헤더 key: " + key + ", value(s): " + values);
        });

        switch (accessor.getCommand()) {
            case CONNECT -> chatSessionEventService.handleConnect(accessor); //STOMP 세션 연결 성공
            case SUBSCRIBE -> chatSessionEventService.handleSubscribe(accessor); //채팅 구독 요청
            case DISCONNECT -> chatSessionEventService.handleDisconnect(accessor);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        Map<String, List<String>> nativeHeaderMap = accessor.toNativeHeaderMap();
        System.out.println("PostSend Accessor");
        nativeHeaderMap.forEach((key,values) ->{
            System.out.println("헤더 key: " + key + ", value(s): " + values);
        });
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Principal user = (Principal) accessor.getSessionAttributes().get("userPrincipal");
            System.out.println("user = " + user);
            if (user instanceof CustomPrincipal customPrincipal) {
                String nickname = customPrincipal.nickname();
                accessor.setNativeHeader("user-name", nickname != null && !nickname.isBlank() ? nickname : "unknown");
            } else {
                accessor.setNativeHeader("user-name", "unknown");
            }
        }

        String usernameHeader = accessor.getFirstNativeHeader("user-name");
        log.info("user-name header: {}", usernameHeader);
    }
}
