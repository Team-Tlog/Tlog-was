package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageReadDto;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatStompController {  // WebSocket 전용
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(@Payload @Valid ChatMessageRequestDto chatMessageRequestDto) {
        if (chatMessageRequestDto.senderId() == null || chatMessageRequestDto.chatRoomId() == null) {
            log.warn("Invalid message payload: {}", chatMessageRequestDto);
            throw new IllegalArgumentException("senderId와 chatRoomId는 필수입니다.");
        }
        chatService.sendChatMessage(chatMessageRequestDto);
    }

    @MessageMapping("/chat/read")
    @Transactional
    public void handleReadMessage(ChatMessageReadDto readDto) {
        chatService.checkMessage(readDto);
    }
}
