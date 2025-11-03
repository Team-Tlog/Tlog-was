package com.se.Tlog.domain.Social.Chat.message.dto;

import java.util.List;

public record ChatHistoryResponse(
        List<ChatMessageDto> messages,
        Long nextCursor,
        boolean hasMore

) {
    public static ChatHistoryResponse from(
            List<ChatMessageDto> messages,
            Long nextCursor,
            boolean hasMore) {
        return new ChatHistoryResponse(messages, nextCursor, hasMore);
    }
}
