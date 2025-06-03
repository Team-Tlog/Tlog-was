package com.se.Tlog.domain.User.controller.dto;

import com.se.Tlog.domain.User.domain.User;

public record UserProfileInfo(
        String imageUrl
) {
    public static UserProfileInfo of(User user){
        return new UserProfileInfo(
                user.getProfileImage()
        );
    }
    
    public static UserProfileInfo getNullProfile() {
        return new UserProfileInfo("" /*, "탈퇴한 사용자"*/);
    }
}
