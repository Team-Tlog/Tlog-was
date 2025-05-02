package com.se.Tlog.domain.Social.Chat.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "특정 채팅 메시지 읽음 처리 요청 DTO")
public record ChatMessageReadDto(
        @Schema(description = "메시지를 읽은 사용자의 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID readerId,
        @Schema(description = "읽은 채팅 메시지의 ID", example = "12345")
        Long messageId
) {
}
