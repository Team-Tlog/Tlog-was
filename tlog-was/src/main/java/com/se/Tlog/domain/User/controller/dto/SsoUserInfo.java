package com.se.Tlog.domain.User.controller.dto;

public record SsoUserInfo(
        String providerId,
        String email,
        String nickname,
        String provider
) {
    public String getProviderUserInfo() {
        return provider + " " + providerId;
    }
}
