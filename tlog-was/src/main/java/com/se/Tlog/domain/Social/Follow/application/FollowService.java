package com.se.Tlog.domain.Social.Follow.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Follow.controller.dto.FollowDto;
import com.se.Tlog.domain.Social.Follow.controller.dto.FollowStatusDto;
import com.se.Tlog.domain.Social.Follow.domain.Follow;
import com.se.Tlog.domain.Social.Follow.repository.jpa.FollowRepository;
import com.se.Tlog.domain.User.controller.dto.UserSummaryDto;
import com.se.Tlog.domain.User.domain.service.UserDomainService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserDomainService userDomainService;

    public FollowStatusDto follow(FollowDto followDto) {

        userDomainService.validateExists(followDto.from_userId());
        userDomainService.validateExists(followDto.to_userId());

        Optional<Follow> existing = followRepository.findByFromUserIdAndToUserId(followDto.from_userId(), followDto.to_userId());

        if(existing.isPresent()){
            // 이미 팔로우 -> 언팔로우 처리
            followRepository.delete(existing.get());
            return FollowStatusDto.from(false);
        }else{
            // 팔로우 처리
            Follow follow = Follow.follow(followDto.from_userId(), followDto.to_userId());
            followRepository.save(follow);
            return FollowStatusDto.from(true);
        }
    }

    public Page<UserSummaryDto> getFollowingList(UUID fromUserId, Pageable pageable) {
        userDomainService.validateExists(fromUserId);

        Page<UUID> pagedToUserIds = followRepository.findToUserIdsByFromUserId(fromUserId,pageable);

        List<UserSummaryDto> userSummaryDtoList = userDomainService.getUserByIds(pagedToUserIds.getContent());

        return new PageImpl<>(userSummaryDtoList, pageable, pagedToUserIds.getTotalElements());
    }

    public Page<UserSummaryDto> getFollowerList(UUID toUserId, Pageable pageable) {
        userDomainService.validateExists(toUserId);

        Page<UUID> pagedFromUserIds = followRepository.findFromUserIdsByToUserId(toUserId,pageable);

        List<UserSummaryDto> userSummaryDtoList = userDomainService.getUserByIds(pagedFromUserIds.getContent());

        return new PageImpl<>(userSummaryDtoList, pageable, pagedFromUserIds.getTotalElements());

    }
}
