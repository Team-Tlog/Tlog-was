package com.se.Tlog.domain.Social.Chat.message.dto;


import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatMessageRequestDto(
        UUID senderId,
        Long chatRoomId,

        @Size(max = 1000)
        String content
) {
}
