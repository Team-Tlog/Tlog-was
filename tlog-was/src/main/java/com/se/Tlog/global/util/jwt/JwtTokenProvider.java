package com.se.Tlog.global.util.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface JwtTokenProvider {
    String generateToken(String userId, String role);

    boolean isTokenExpired(String token);

    Claims parseToken(String token);

    Claims parseAndValidate(String token);

    Date getExpiration(String token);
}
