package com.se.Tlog.domain.Team.controller.dto;

import java.util.UUID;

public record TeamCreateRes(
        UUID teamId,
        Long chatRoomId
) {
    public static TeamCreateRes of(UUID teamId, Long chatRoomId) {
        return new TeamCreateRes(teamId, chatRoomId);
    }
}
