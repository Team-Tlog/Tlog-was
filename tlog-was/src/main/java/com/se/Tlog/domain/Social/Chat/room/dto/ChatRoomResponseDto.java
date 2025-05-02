package com.se.Tlog.domain.Social.Chat.room.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.List;
import java.util.UUID;

public record ChatRoomResponseDto(
        Long roomId,
        UUID hostId,
        List<ChatUserSummaryDto> chatUserSummaryDtoList
) {
    public static ChatRoomResponseDto from(Long roomId, UUID hostId, List<User> users) {
        return new ChatRoomResponseDto(
                roomId,
                hostId,
                users.stream().map(ChatUserSummaryDto::from).toList()
        );
    }
}
