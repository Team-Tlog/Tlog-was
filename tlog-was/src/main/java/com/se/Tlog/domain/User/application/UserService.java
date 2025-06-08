package com.se.Tlog.domain.User.application;


import com.se.Tlog.domain.Reward.application.RewardService;
import com.se.Tlog.domain.Reward.controller.dto.UserRewardDto;
import com.se.Tlog.domain.Tbti.application.TbtiInfoService;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiInfoRes;
import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.User.controller.dto.TlogMyPageRes;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;
    private final RewardService rewardService;
    private final TbtiInfoService tbtiInfoService;

    /*@Transactional
    public void updateEmail(UUID userId, String email) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        user.updateEmail(email);
    }*/

    @Transactional
    public String updateSnsId(UUID userId, String snsId) {
        if (userRepository.existsBySnsId(snsId)) {
            throw new CustomException(ErrorType.ALREADY_EXISTS_SNSId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        user.updateSnsId(snsId);
        // 위험 : 이전 토큰이 무효화되지 않아 이전 토큰이 여전히 사용가능!
        return accessTokenProvider.generateToken(
                user.getId().toString(),
                user.getRole().getValue(),
                user.getSnsId(),
                user.getName()
        );
    }
    
    @Transactional
    public TbtiInfoRes updateUserTbti(UUID userId, int tbtiValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        Tbti newTbti = new Tbti(tbtiValue);
        user.updateTbti(newTbti);
        return tbtiInfoService.getTbtiInfo(newTbti);
    }

    @Transactional
    public void uploadProfileImage(String userId, String imageUrl) {
        if (!imageUrl.contains("firebasestorage.googleapis.com")) {
            throw new CustomException(ErrorType.INVALID_IMAGE_URL);
        }
            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

            user.updateProfileImage(imageUrl);
    }
    
    public TlogMyPageRes getTlogMyPage(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        List<UserRewardDto> rewards = rewardService.getAllRewardOfUser(userId);
        String defaultRwardPhrase = "아직 기본 보상이 설정되지 않았어요!";
        for (UserRewardDto reward : rewards)
            if (reward.isDefaultReward())
                defaultRwardPhrase = reward.description();
        TbtiInfoRes tbtiInfo = tbtiInfoService.getTbtiInfo(new Tbti(user.getTbti()).toString());
        
        return new TlogMyPageRes(
                user.getName(), 
                user.getProfileImage(),
                user.getSnsId(),
                defaultRwardPhrase,
                rewards,
                tbtiInfo);
    }
}
