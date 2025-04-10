package com.se.Tlog.domain.Social.Chat.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "초대할 유저들의 UUID 리스트 요청 DTO")
public record RequestInviteList(
        @Schema(description = "초대할 유저들의 UUID 리스트")
        List<UUID> inviteList
) {

}
