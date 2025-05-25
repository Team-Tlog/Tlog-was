package com.se.Tlog.domain.Social.Chat.room.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record ChatRoomResponseDto(
        Long roomId,
        UUID hostId,
        ChatUserSummaryDto chatUserSummaryDtoList
) {
    public static ChatRoomResponseDto from(Long roomId, UUID hostId, User user) {
        return new ChatRoomResponseDto(
                roomId,
                hostId,
                ChatUserSummaryDto.from(user)
        );
    }
}
