package com.se.Tlog.global.util.jwt;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class AccessTokenProvider implements JwtTokenProvider {

    private final JwtUtil jwtUtil;

    @Value("${JWT_ACCESS_EXPIRATION}")
    private Duration accessTokenDuration;

    public String generateToken(String userId, String role, String snsId,String nickname) {
        Map<String, Object> claims = Map.of(
                "role", role,
                "tokenType", "access",
                "jti", UUID.randomUUID().toString(),
                "snsId", snsId == null ? "" : snsId,
                "nickname", nickname
        );
        return jwtUtil.generateToken(userId,accessTokenDuration,claims);
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = jwtUtil.parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.warn("JWT 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Claims parseToken(String token) {
        return jwtUtil.parseToken(token);
    }

    @Override
    public Claims parseTokenIgnoringExpiration(String token) {
        return jwtUtil.parseTokenIgnoringExpiration(token);
    }

    @Override
    public Claims parseAndValidate(String token) {
        Claims claims = parseToken(token);
        if (claims.getExpiration().before(new Date())) {
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        }
        return claims;
    }


}
