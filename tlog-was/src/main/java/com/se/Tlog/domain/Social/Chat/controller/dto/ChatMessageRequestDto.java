package com.se.Tlog.domain.Social.Chat.controller.dto;


import java.util.UUID;

public record ChatMessageRequestDto(
        UUID senderId,
        Long chatRoomId,
        String content
) {
}
