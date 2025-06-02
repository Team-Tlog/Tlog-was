package com.se.Tlog.domain.User.application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.se.Tlog.domain.User.repository.api.SsoService;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.User.controller.dto.LoginRequest;
import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.controller.dto.TokenDto;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import com.se.Tlog.global.util.redis.RedisProperties;
import com.se.Tlog.global.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class SsoAuthService {
    private final Map<SsoType, SsoService> ssoServiceMap;
    private final UserRepository userRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RedisUtil redisUtil;
    
    private SsoUserInfo getSsoUserInfo(SsoType type, String accessToken) {
        SsoService ssoService = Optional.ofNullable(ssoServiceMap.get(type))
                .orElseThrow(() -> new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN));

        SsoUserInfo ssoUserInfo = ssoService.getUserInfo(accessToken);
        System.out.println("ssoUserInfo = " + ssoUserInfo);
        return ssoUserInfo;
    }
    
    private TokenDto loginUser(User user) {
        String accessToken = accessTokenProvider.generateToken(user.getId().toString(), user.getRole().getValue(),user.getSnsId());
        String refreshToken = refreshTokenProvider.generateToken(user.getId().toString(), user.getRole().getValue());

        String jti = refreshTokenProvider.parseToken(refreshToken).get("jti").toString();
        String refreshKey = RedisProperties.REFRESH_TOKEN_PREFIX + user.getId() + ":" + jti;

        redisUtil.save(refreshKey, refreshToken, refreshTokenProvider.getRefreshTokenDuration());

        String customToken = "";
        try{
            customToken = FirebaseAuth.getInstance().createCustomToken(user.getId().toString());
        }catch (FirebaseAuthException e) {
            throw new CustomException(ErrorType.FIREBASE_CUSTOM_TOKEN_ISSUE_FAIL);
        }

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firebaseCustomToken(customToken)
                .build();
    }
    
    private User registerNewUser(SsoUserInfo ssoUserInfo) {
        return userRepository.save(
                User.create(ssoUserInfo));
    }
    
    public TokenDto login(LoginRequest loginRequest) {
        SsoUserInfo ssoUserInfo = getSsoUserInfo(loginRequest.type(), loginRequest.accessToken());
        User user = userRepository.findByProviderUserInfo(ssoUserInfo.getProviderUserInfo())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_REGISTERED));
        return loginUser(user);
    }
    
    public TokenDto register(LoginRequest loginRequest) {
        SsoUserInfo ssoUserInfo = getSsoUserInfo(loginRequest.type(), loginRequest.accessToken());
        if (userRepository.findByProviderUserInfo(ssoUserInfo.getProviderUserInfo())
                .isPresent())
            throw new CustomException(ErrorType.ALREADY_REGISTERED);
        return loginUser(registerNewUser(ssoUserInfo));
    }
    
    public void logout(String accessToken,String refreshToken) {
        Claims accessClaims = accessTokenProvider.parseToken(accessToken);
        String accessJti = accessClaims.get("jti").toString();

        long remainingTime = accessTokenProvider.getExpiration(accessToken).getTime() - System.currentTimeMillis();

        Claims refreshClaims = refreshTokenProvider.parseToken(refreshToken);
        String refreshJti = refreshClaims.get("jti").toString();
        String refreshKey = RedisProperties.REFRESH_TOKEN_PREFIX + refreshClaims.getSubject() + ":" + refreshJti;

        try {
            redisUtil.setBlacklistToken(RedisProperties.ACCESS_TOKEN_PREFIX + accessJti, remainingTime);
        } catch (Exception e) {
            log.error("블랙리스트 등록 실패: {}", accessJti, e);
            throw new CustomException(ErrorType.BLACKLIST_SAVE_FAILED);
        }

        boolean isDeleted = redisUtil.delete(refreshKey);
        if (!isDeleted) {
            log.warn("Refresh token 삭제 실패: {}", refreshKey);
        }
    }

}
