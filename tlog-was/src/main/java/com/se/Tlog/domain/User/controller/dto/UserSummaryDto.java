package com.se.Tlog.domain.User.controller.dto;

import com.se.Tlog.domain.User.domain.User;

import java.util.UUID;

public record UserSummaryDto(
        UUID uuid,
        String name,
        String snsName,
        String profileImageUrl,
        int tbtiValue
) {
    public static UserSummaryDto from(User user){
        return new UserSummaryDto(
                user.getId(), 
                user.getName(), 
                user.getSnsId(), 
                user.getProfileImage(), 
                user.getTbti());
    }
}
