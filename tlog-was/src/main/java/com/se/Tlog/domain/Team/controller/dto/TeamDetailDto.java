package com.se.Tlog.domain.Team.controller.dto;

import com.se.Tlog.domain.Team.domain.InviteCodeUtil;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema
public record TeamDetailDto(
        UUID teamId,
        @Schema(example = "나의 팀")
        String teamName,
        @Schema(example = "RENA")
        String tbtiString,
        @Schema(example = "aBcdEF")
        String inviteCode,
        Long chatRoomId,
        LocalDate createdAt,
        LocalDate expiredAt,
        List<TeamMemberDto> members,
        List<WishlistDestinationRes> wishlist,
        TravelPlanDto travelPlanDto
) {
    public static TeamDetailDto from(Team team, Long chatRoomId, List<TeamMemberDto> members,
                                     List<WishlistDestinationRes> wishlist, TravelPlanDto travelPlanDto) {
        return new TeamDetailDto(
                team.getId(),
                team.getName(),
                team.getTbtiString(), // 만약 TBTI 설명까지 추가해야 할 경우, 여기서 처리하지 않고 외부에서 설명 DTO를 파라미터로 받을 것!
                InviteCodeUtil.longToStr(team.getInviteCode()),
                chatRoomId,
                team.getCreatedAt().toLocalDate(),
                LocalDate.now(),
                members,
                wishlist,
                travelPlanDto
        );
    }
}
