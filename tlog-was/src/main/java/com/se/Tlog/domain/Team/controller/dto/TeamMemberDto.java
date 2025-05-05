package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record TeamMemberDto(
        UUID userId,
        String name,
        boolean isLeader
        // profile, tbti
) {
    public static TeamMemberDto from(User user, boolean isLeader) {
        return new TeamMemberDto(
                user.getId(),
                user.getName(),
                isLeader
        );
    }
}
