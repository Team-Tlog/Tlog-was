package com.se.Tlog.domain.Social.Chat.message.dto;

public record MessageReadUpdateDto(
        Long messageId,
        int newUnreadCount
) {
    public static MessageReadUpdateDto from(Long messageId, int newUnreadCount) {
        return new MessageReadUpdateDto(messageId, newUnreadCount);
    }
}
