package com.se.Tlog.domain.Social.Chat.controller.dto;

import java.util.UUID;

public record ChatMessageCheckDto(
        UUID readerId,
        Long messageId
) {
}
