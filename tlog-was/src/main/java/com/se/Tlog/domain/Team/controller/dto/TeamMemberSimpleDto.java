package com.se.Tlog.domain.Team.controller.dto;


import java.util.UUID;

public record TeamMemberSimpleDto(
        UUID memberId,
        String tbtiString
) {
    public static TeamMemberSimpleDto from(UUID userId,String tbtiString) {
        return new TeamMemberSimpleDto(
                userId,
                tbtiString
        );
    }
}
