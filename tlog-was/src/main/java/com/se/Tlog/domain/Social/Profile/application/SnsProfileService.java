package com.se.Tlog.domain.Social.Profile.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Follow.repository.jpa.FollowRepository;
import com.se.Tlog.domain.Social.Post.application.PostService;
import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;
import com.se.Tlog.domain.Social.Profile.controller.dto.SnsProfileRes;
import com.se.Tlog.domain.Social.Profile.controller.dto.UpdateSnsDescriptionReq;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SnsProfileService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostService postService;
    
    private static final int POST_COUNT_OF_FIRST_PAGE = 24;
    private static final String DEFAULT_SNS_DESCRIPTION = "나에 대한 설명글을 입력하세요";
    
    @Transactional
    public void updateSnsDescription(UUID userId, UpdateSnsDescriptionReq request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        if (request == null || request.description() == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        user.updateSnsDescription(request.description());
    }
    
    public SnsProfileRes getSnsProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        String snsDescription = (null == user.getSnsDescription())
                ? DEFAULT_SNS_DESCRIPTION
                : user.getSnsDescription();
        
        int followerCount = followRepository.countByToUserId(userId);
        int followingCount = followRepository.countByFromUserId(userId);
        Page<PostPreviewRes> posts = postService.getPreviewUserPosts(userId, PageRequest.of(0, POST_COUNT_OF_FIRST_PAGE));
        
        return new SnsProfileRes(
                user.getSnsId(),
                user.getProfileImage(),
                snsDescription,
                posts.getTotalElements(),
                followerCount,
                followingCount,
                posts);
    }
}
