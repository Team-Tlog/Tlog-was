package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.User.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record TeamMemberDto(
        UUID userId,
        String profileImageUrl,
        String name,
        @Schema(example = "RENA")
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
