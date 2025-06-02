package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.Team.domain.InviteCodeUtil;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Travel.controller.dto.SimpleDestinationRes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TeamDetailDto(
        UUID teamId,
        String teamName,
        //String teamTBTI,
        String inviteCode,
        LocalDate startDate,
        LocalDate endDate,
        List<TeamMemberDto> members,
        List<SimpleDestinationRes> wishlist
) {
    public static TeamDetailDto from(Team team, List<TeamMemberDto> members, List<SimpleDestinationRes> wishlist) {
        return new TeamDetailDto(
                team.getId(),
                team.getName(),
                InviteCodeUtil.longToStr(team.getInviteCode()),
                team.getCreatedAt().toLocalDate(),
                LocalDate.now(),
                members,
                wishlist
        );
    }
}
