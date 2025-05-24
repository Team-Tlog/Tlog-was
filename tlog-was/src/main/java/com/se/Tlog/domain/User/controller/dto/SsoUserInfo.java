package com.se.Tlog.domain.User.controller.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record SsoUserInfo(
        String providerId,
        String email,
        String nickname,
        String provider
) {
    public static SsoUserInfo from(JsonNode jsonNode, String provider) {
        System.out.println("jsonNode = " + jsonNode);

        String providerId = jsonNode.path("id").asText();
        String email = "";
        String nickname = "";

        switch (provider) {
            case "kakao" -> {
                JsonNode accountNode = jsonNode.path("kakao_account");
                nickname = accountNode.path("profile").path("name").asText("");
            }
            case "google" -> {
                email = jsonNode.path("email").asText("");
                nickname = jsonNode.path("name").asText("");
            }
        }

        return new SsoUserInfo(providerId, email, nickname, provider);
    }
}
