package com.se.Tlog.domain.Social.Chat.message.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(
        UUID senderId,
        Long chatRoomId,
        String senderName,
        String content,
        LocalDateTime sendAt
) {
    public static ChatMessageDto from(UUID senderId, String senderName, String content, LocalDateTime sendAt) {

    }
}
