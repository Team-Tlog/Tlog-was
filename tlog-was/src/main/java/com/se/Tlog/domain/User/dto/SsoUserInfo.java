package com.se.Tlog.domain.User.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record SsoUserInfo(
        String providerId,
        String email,
        String nickname,
        String provider
) {
    public static SsoUserInfo from(JsonNode jsonNode, String provider) {
        String providerId = jsonNode.path("id").asText();
        String email = jsonNode.path("email").asText();
        String nickname = jsonNode.path("nickname").asText();

        return new SsoUserInfo(providerId, email, nickname, provider);
    }
}
