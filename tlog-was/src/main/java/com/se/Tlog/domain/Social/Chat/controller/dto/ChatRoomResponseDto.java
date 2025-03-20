package com.se.Tlog.domain.Social.Chat.controller.dto;

import java.util.UUID;

public record ChatRoomResponseDto(
        Long roomId,
        UUID hostId
) {
    public static ChatRoomResponseDto from(Long roomId, UUID hostId) {
        return new ChatRoomResponseDto(roomId, hostId);
    }
}
