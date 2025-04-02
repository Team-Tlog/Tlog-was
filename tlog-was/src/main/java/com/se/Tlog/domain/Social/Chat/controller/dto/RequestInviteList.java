package com.se.Tlog.domain.Social.Chat.controller.dto;

import java.util.List;
import java.util.UUID;

public record RequestInviteList(
        List<UUID> inviteList
) {

}
