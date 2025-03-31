package com.se.Tlog.domain.User.application;

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
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class SsoAuthService {
    private final Map<SsoType, SsoService> ssoServiceMap;
    private final UserRepository userRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RedisUtil redisUtil;

    public TokenDto login(LoginRequest loginRequest){
        SsoService ssoService = Optional.ofNullable(ssoServiceMap.get(loginRequest.type()))
                .orElseThrow(() -> new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN));

        SsoUserInfo ssoUserInfo = ssoService.getUserInfo(loginRequest.accessToken());
        System.out.println("ssoUserInfo = " + ssoUserInfo);
        String providerUserInfo = ssoUserInfo.provider() + " " + ssoUserInfo.providerId();

        User user = userRepository.findByProviderUserInfo(providerUserInfo)
                .orElseGet(() -> userRepository.save(User.create(ssoUserInfo)));

        String accessToken = accessTokenProvider.generateToken(user.getId().toString(), user.getRole().getValue());
        String refreshToken = refreshTokenProvider.generateToken(user.getId().toString(), user.getRole().getValue());

        redisUtil.save(RedisProperties.REFRESH_TOKEN_PREFIX + user.getId(), refreshToken, refreshTokenProvider.getRefreshTokenDuration());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
