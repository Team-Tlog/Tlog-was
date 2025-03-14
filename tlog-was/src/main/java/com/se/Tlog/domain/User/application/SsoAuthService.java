package com.se.Tlog.domain.User.application;

import com.se.Tlog.domain.User.presentation.dto.LoginRequest;
import com.se.Tlog.domain.User.presentation.dto.SsoUserInfo;
import com.se.Tlog.domain.User.presentation.dto.TokenDto;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.infrastructure.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.JwtUtil;
import com.se.Tlog.global.util.redis.RedisProperties;
import com.se.Tlog.global.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SsoAuthService {
    private final Map<SsoType, SsoService> ssoServiceMap;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public TokenDto login(LoginRequest loginRequest){
        SsoService ssoService = Optional.ofNullable(ssoServiceMap.get(loginRequest.type()))
                .orElseThrow(() -> new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN));

        SsoUserInfo ssoUserInfo = ssoService.getUserInfo(loginRequest.accessToken());
        System.out.println("ssoUserInfo = " + ssoUserInfo);
        String providerUserInfo = ssoUserInfo.provider() + " " + ssoUserInfo.providerId();

        User user = userRepository.findByProviderUserInfo(providerUserInfo)
                .orElseGet(() -> userRepository.save(User.create(ssoUserInfo)));

        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().getValue());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString(), user.getRole().getValue());

        redisUtil.save(RedisProperties.REFRESH_TOKEN_PREFIX + user.getId(), refreshToken, jwtUtil.getRefreshTokenDuration());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
