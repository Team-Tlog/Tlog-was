package com.se.Tlog.global.util.redis;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j

@Component
@RequiredArgsConstructor
public class RedisTokenUtil {
    private final RedisUtil redisUtil;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public void registerRefreshToken(UUID userId, String refreshToken) {
        String jti = refreshTokenProvider.parseToken(refreshToken).get("jti").toString();
        String refreshKey = RedisProperties.REFRESH_TOKEN_PREFIX + userId + ":" + jti;

        redisUtil.save(refreshKey, refreshToken, refreshTokenProvider.getRefreshTokenDuration());
    }

    /**
     *
     * @throws CustomException
     * 블랙리스트 설정에 실패할 경우 에러가 발생합니다.
     */
    public void disableAccessToken(String accessToken) throws CustomException {
        Claims accessClaims = accessTokenProvider.parseToken(accessToken);

        String accessJti = accessClaims.get("jti").toString();
        String accessKey = RedisProperties.ACCESS_TOKEN_PREFIX + accessJti;
        long remainingTime = accessClaims.getExpiration().getTime() - System.currentTimeMillis();

        try {
            redisUtil.setBlacklistToken(accessKey, remainingTime);
        } catch (Exception e) {
            log.error("블랙리스트 등록 실패: {}", accessJti, e);
            throw new CustomException(ErrorType.BLACKLIST_SAVE_FAILED);
        }
    }

    public void disableRefreshToken(String refreshToken) {
        Claims refreshClaims = refreshTokenProvider.parseToken(refreshToken);

        String refreshJti = refreshClaims.get("jti").toString();
        String refreshKey = RedisProperties.REFRESH_TOKEN_PREFIX + refreshClaims.getSubject() + ":" + refreshJti;

        boolean isDeleted = redisUtil.delete(refreshKey);
        if (!isDeleted) {
            log.warn("Refresh token 삭제 실패: {}", refreshKey);
        }
    }

    public boolean isAccessTokenBlackListed(String accessToken) {
        Claims claims = accessTokenProvider.parseToken(accessToken);
        String jti = claims.get("jti").toString();
        String accessKey = RedisProperties.ACCESS_TOKEN_PREFIX + jti;

        return redisUtil.isTokenBlacklisted(accessKey);
    }
}
