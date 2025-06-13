package com.se.Tlog.domain.User.controller.dto;

import com.se.Tlog.domain.User.domain.User;

public record UserProfileInfo(
        String imageUrl,
        String username
) {
    public static UserProfileInfo of(User user){
        return new UserProfileInfo(
                user.getProfileImage(),
                user.getName()
        );
    }
    
    public static UserProfileInfo getNullProfile() {
        return new UserProfileInfo("" , "탈퇴한 사용자");
    }
}
