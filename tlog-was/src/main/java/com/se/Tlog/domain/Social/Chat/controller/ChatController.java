package com.se.Tlog.domain.Social.Chat.controller;

import com.se.Tlog.domain.Social.Chat.controller.dto.ChatMessageReadDto;
import com.se.Tlog.domain.Social.Chat.controller.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import com.se.Tlog.domain.Social.Chat.service.ChatService;
import com.se.Tlog.domain.Social.Chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageRequestDto chatMessageRequestDto) {
        if (chatMessageRequestDto.senderId() == null || chatMessageRequestDto.chatRoomId() == null) {
            log.warn("Invalid message payload: {}", chatMessageRequestDto);
            throw new IllegalArgumentException("senderId와 chatRoomId는 필수입니다.");
        }

        ChatMessage message = chatService.sendChatMessage(chatMessageRequestDto);
        redisPublisher.publish(new ChannelTopic("chat"),message);
    }

    @PatchMapping("/chat/room/{roomId}/message")
    public void checkMessage(@RequestBody ChatMessageReadDto chatMessageCheckDto) {

        chatService.checkMessage(chatMessageCheckDto);
    }


}
