package com.se.Tlog.global.security.handler;

import io.jsonwebtoken.Claims;
import java.security.Principal;
import java.util.UUID;


public record CustomPrincipal(
        UUID userId,
        String nickname,
        String role
) implements Principal {

    @Override
    public String getName() {
        return userId.toString();
    }

    public static CustomPrincipal from(Claims claims) {
        return new CustomPrincipal(
                UUID.fromString(claims.getSubject()),
                (String) claims.get("nickname"),
                (String) claims.get("role"));
    }
}
