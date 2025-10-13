package com.se.Tlog.domain.Team.controller.dto;


import com.se.Tlog.domain.User.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record TeamMemberSimpleDto(
        UUID memberId,
        @Schema(description = "팀원의 TBTI 유형입니다.", example = "RENA")
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
