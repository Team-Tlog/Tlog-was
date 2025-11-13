package com.se.Tlog.domain.User.application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.se.Tlog.domain.User.repository.api.SsoService;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.User.repository.jpa.UserTagRepository;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.Travel.domain.TagType;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;
import com.se.Tlog.domain.User.controller.dto.LoginRequest;
import com.se.Tlog.domain.User.controller.dto.RegisterRequest;
import com.se.Tlog.domain.User.controller.dto.RegisterUserProfileDto;
import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.controller.dto.TokenDto;
import com.se.Tlog.domain.User.controller.dto.UserTagRes;
import com.se.Tlog.domain.User.domain.PreferPhoto;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.UserRegisterInfo;
import com.se.Tlog.domain.User.domain.UserTagInfo;
import com.se.Tlog.domain.User.repository.mongo.PreferPhotoRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import com.se.Tlog.global.util.redis.RedisProperties;
import com.se.Tlog.global.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class SsoAuthService {
    private final Map<SsoType, SsoService> ssoServiceMap;
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final TagRepository tagRepository;
    private final PreferPhotoRepository preferPhotoRepository;
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
        String accessToken = accessTokenProvider.generateToken(user.getId().toString(), user.getRole().getValue(), user.getSnsId(), user.getName());
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

    private Map<TagType, Long> getPreferTagCount(Map<TagType, Tag> allTags, List<Integer> photoIds) {
        if (photoIds != null && !photoIds.isEmpty()) {
            List<PreferPhoto> photos = preferPhotoRepository.findAllById(photoIds);
            List<String> tagIds = photos.stream()
                    .flatMap(photo -> photo.getTagIds().stream())
                    .toList(); // 25.9.23 추후 겹치는 태그를 더 강하게 표시하는 등의 목적을 위해 중복값을 허용합니다.

            Map<String, Tag> tags = allTags.values().stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
            return tagIds.stream()
                    .collect(Collectors.groupingBy(id -> tags.get(id).getTagType(), Collectors.counting()));
        }
        return Map.of();
    }
    
    private User registerNewUser(SsoUserInfo ssoUserInfo, RegisterUserProfileDto userProfiles) {
        User newUser = userRepository.save(
                User.create(new UserRegisterInfo(ssoUserInfo, userProfiles)));

        Map<TagType, Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> tag.getTagType() != null)
                .collect(Collectors.toMap(Tag::getTagType, Function.identity()));
        Map<TagType, Long> preferTagCount = getPreferTagCount(tags, userProfiles.preferPhotoIds());
        userTagRepository.saveAll(UserTagInfo.of(newUser, tags, preferTagCount));
        return newUser;
    }


    
    public TokenDto login(LoginRequest loginRequest) {
        SsoUserInfo ssoUserInfo = getSsoUserInfo(loginRequest.type(), loginRequest.accessToken());
        User user = userRepository.findByProviderUserInfo(ssoUserInfo.getProviderUserInfo())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_REGISTERED));
        return loginUser(user);
    }
    
    public TokenDto register(RegisterRequest registerRequest) {
        SsoUserInfo ssoUserInfo = getSsoUserInfo(registerRequest.type(), registerRequest.accessToken());
        if (userRepository.findByProviderUserInfo(ssoUserInfo.getProviderUserInfo())
                .isPresent())
            throw new CustomException(ErrorType.ALREADY_REGISTERED);
        return loginUser(registerNewUser(ssoUserInfo, registerRequest.userProfile()));
    }
    
    public void logout(String accessToken,String refreshToken) {
        Claims accessClaims = accessTokenProvider.parseToken(accessToken);
        String accessJti = accessClaims.get("jti").toString();

        long remainingTime = accessClaims.getExpiration().getTime() - System.currentTimeMillis();

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

    @Deprecated(since = "테스트 환경 전용입니다.")
    public List<UserTagRes> getUserTags(RegisterUserProfileDto request) {
        Map<TagType, Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> tag.getTagType() != null)
                .collect(Collectors.toMap(Tag::getTagType, Function.identity()));
        Map<TagType, Double> tagWeightMap = UserTagInfo.getTagWeightMap(
                new Tbti(Integer.parseInt(request.tbtiValue())),
                tags,
                getPreferTagCount(tags, request.preferPhotoIds()));
        return tagWeightMap.keySet().stream()
                .map(type -> new UserTagRes(
                        tags.get(type).getName(),
                        tagWeightMap.get(type)))
                .toList();
    }
}
