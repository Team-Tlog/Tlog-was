package com.se.Tlog.domain.Social.Chat.message.dto;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamChatSummaryDto(
        UUID userId,
        Long roomId,
        String content,
        int unreadCount,
        LocalDateTime sendAt
) {
    public static TeamChatSummaryDto from(UUID userId, Long roomId, ChatMessage chatMessage, int unreadCount) {
        return new TeamChatSummaryDto(userId, roomId, chatMessage.getContent(), unreadCount, chatMessage.getSendAt());
    }
}
