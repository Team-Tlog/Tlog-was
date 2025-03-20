package com.se.Tlog.domain.Social.Follow.controller.dto;

import java.util.UUID;

public record FollowDto (
        UUID from_userId,
        UUID to_userId
){

}
