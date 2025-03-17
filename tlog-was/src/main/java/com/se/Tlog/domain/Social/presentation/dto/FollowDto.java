package com.se.Tlog.domain.Social.presentation.dto;

import java.util.UUID;

public record FollowDto (
        UUID from_userId,
        UUID to_userId
){

}
