package com.se.Tlog.domain.Social.Chat.controller.dto;

import java.time.LocalDateTime;

public record ChatRoomListResponseDto(
        Long chatRoomId,
        String lastMessageContent,
        LocalDateTime lastMessageSentAt,
        int countChatRoomUsers,
        int unreadCount
) {
    public static ChatRoomListResponseDto from(Long chatRoomId, String lastMessageContent,
                                               LocalDateTime lastMessageSentAt,
                                               int countChatRoomUsers,int unreadCount) {
        return new ChatRoomListResponseDto(
                chatRoomId, lastMessageContent,
                lastMessageSentAt,countChatRoomUsers,
                unreadCount);
        }
}
