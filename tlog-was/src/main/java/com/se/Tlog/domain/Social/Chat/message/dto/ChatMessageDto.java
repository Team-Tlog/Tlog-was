package com.se.Tlog.domain.Social.Chat.message.dto;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(
        Long id,
        UUID senderId,
        String senderName,
        Long chatRoomId,
        String content,
        LocalDateTime sendAt,
        int unreadCount
) {
    public static ChatMessageDto from(ChatMessage message, int readCount, int totalParticipants) {
        return new ChatMessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getChatRoom().getId(),
                message.getContent(),
                message.getSendAt(),
                totalParticipants - readCount
        );
    }
}
