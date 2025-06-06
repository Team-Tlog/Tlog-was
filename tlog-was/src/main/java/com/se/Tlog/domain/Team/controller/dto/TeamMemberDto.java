package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record TeamMemberDto(
        UUID userId,
        String profileImageUrl,
        String name,
        String tbtiString,
        boolean isLeader
) {
    public static TeamMemberDto from(User user, boolean isLeader) {
        return new TeamMemberDto(
                user.getId(),
                user.getProfileImage(),
                user.getName(),
                user.getTbtiString(),
                isLeader
        );
    }
}
