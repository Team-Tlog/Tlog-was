package com.se.Tlog.domain.Team.controller.dto;


import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record TeamMemberSimpleDto(
        UUID memberId,
        String tbtiString,
        String profileImage
) {
    public static TeamMemberSimpleDto from(User user) {
        return new TeamMemberSimpleDto(
                user.getId(),
                user.getTbtiString(),
                user.getProfileImage()
        );
    }
}
