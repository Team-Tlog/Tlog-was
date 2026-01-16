package com.se.Tlog.global.util.redis;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j

@Component
@RequiredArgsConstructor
public class RedisTokenUtil {
    private final RedisUtil redisUtil;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    private String getAccessTokenKey(String accessToken) {
        try {
            Claims accessClaims = accessTokenProvider.parseToken(accessToken);
            String accessJti = accessClaims.get("jti").toString();
            return RedisProperties.ACCESS_TOKEN_PREFIX + accessJti;
        } catch (ExpiredJwtException e) {
            log.error("token expired");
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.error("token authentication failed : " + e.getMessage());
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        }
    }

    private String getRefreshTokenKey(String refreshToken) {
        try {
            Claims refreshClaims = refreshTokenProvider.parseToken(refreshToken);
            String jti = refreshTokenProvider.parseToken(refreshToken).get("jti").toString();
            return RedisProperties.REFRESH_TOKEN_PREFIX + refreshClaims.getSubject() + ":" + jti;
        } catch (ExpiredJwtException e) {
            log.error("refresh token expired");
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.error("refresh token authentication failed : " + e.getMessage());
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        }
    }


    public void registerRefreshToken(String refreshToken) {
        redisUtil.save(
                getRefreshTokenKey(refreshToken),
                refreshToken,
                refreshTokenProvider.getRefreshTokenDuration());
    }

    /**
     *
     * @throws CustomException
     * 블랙리스트 설정에 실패할 경우 에러가 발생합니다.
     */
    public void disableAccessToken(String accessToken) throws CustomException {
        String accessKey = getAccessTokenKey(accessToken);
        Claims accessClaims = accessTokenProvider.parseToken(accessToken);
        long remainingTime = accessClaims.getExpiration().getTime() - System.currentTimeMillis();

        try {
            redisUtil.setBlacklistToken(accessKey, remainingTime);
        } catch (Exception e) {
            String accessJti = accessClaims.get("jti").toString();
            log.error("블랙리스트 등록 실패: {}", accessJti, e);
            throw new CustomException(ErrorType.BLACKLIST_SAVE_FAILED);
        }
    }

    public void disableRefreshToken(String refreshToken) {
        String refreshKey = getRefreshTokenKey(refreshToken);
        boolean isDeleted = redisUtil.delete(refreshKey);
        if (!isDeleted) {
            log.warn("Refresh token 삭제 실패: {}", refreshKey);
        }
    }

    public boolean isAccessTokenBlackListed(String accessToken) {
        String accessKey = getAccessTokenKey(accessToken);
        return redisUtil.isTokenBlacklisted(accessKey);
    }
}
