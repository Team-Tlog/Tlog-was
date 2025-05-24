package com.se.Tlog.global.util.jwt;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class RefreshTokenProvider implements JwtTokenProvider{
    private final JwtUtil jwtUtil;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private Duration refreshTokenDuration;

    public String generateToken(String userId, String role) {
        Map<String, Object> claims = Map.of(
                "role", role,
                "tokenType", "refresh",
                "jti", UUID.randomUUID().toString()
        );
        return jwtUtil.generateToken(userId, refreshTokenDuration, claims);
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // 파싱 에러 -> 만료 취급
        }
    }

    @Override
    public Claims parseToken(String token) {
        return jwtUtil.parseToken(token);
    }

    @Override
    public Claims parseAndValidate(String token) {
        Claims claims = parseToken(token);
        if (claims.getExpiration().before(new Date())) {
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        }
        return claims;
    }

    @Override
    public Date getExpiration(String token) {

        return parseToken(token).getExpiration();
    }

    public long getRefreshTokenDuration() {
        return refreshTokenDuration.toMillis();
    }
}
