package com.se.Tlog.domain.Social.Chat.controller.dto;

import java.util.UUID;

public record ChatMessageReadDto(
        UUID readerId,
        Long lastReadMessageId
) {
}
