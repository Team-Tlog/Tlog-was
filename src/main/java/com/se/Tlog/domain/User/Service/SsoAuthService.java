package com.se.Tlog.domain.User.Service;

import com.se.Tlog.domain.User.Entity.User;
import com.se.Tlog.domain.User.SsoType;
import com.se.Tlog.domain.User.dto.LoginRequest;
import com.se.Tlog.domain.User.dto.SsoUserInfo;
import com.se.Tlog.domain.User.dto.TokenDto;
import com.se.Tlog.domain.User.repository.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsoAuthService {
    private final KakaoSsoService kakaoSsoService;
    private final GoogleSsoService googleSsoService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public TokenDto login(LoginRequest loginRequest){
        String ssoAccessToken = loginRequest.accessToken();
        SsoType type = loginRequest.type();

        SsoUserInfo ssoUserInfo = switch (type){
            case KAKAO -> kakaoSsoService.getUserInfo(ssoAccessToken);
            case GOOGLE -> googleSsoService.getUserInfo(ssoAccessToken);
            default -> throw new CustomException(ErrorType.UNSUPPORTED_SSO_LOGIN);
        };
        System.out.println("ssoUserInfo = " + ssoUserInfo);
        String providerUserInfo = ssoUserInfo.provider() + " " + ssoUserInfo.providerId();

        User user = userRepository.findByProviderUserInfo(providerUserInfo)
                .orElseGet(() -> userRepository.save(User.create(ssoUserInfo)));

        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().getValue());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString(), user.getRole().getValue());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
