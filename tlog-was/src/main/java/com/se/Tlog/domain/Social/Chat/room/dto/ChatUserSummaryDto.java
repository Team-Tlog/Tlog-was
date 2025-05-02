package com.se.Tlog.domain.Social.Chat.room.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record ChatUserSummaryDto(
        UUID userId,
        String nickname
        // image?
) {
    public static ChatUserSummaryDto from(User user) {
        return new ChatUserSummaryDto(
                user.getId(),
                user.getName()
        );
    }
}
