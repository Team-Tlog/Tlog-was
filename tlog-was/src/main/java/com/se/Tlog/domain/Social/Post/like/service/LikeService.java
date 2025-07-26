package com.se.Tlog.domain.Social.Post.like.service;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Post.controller.dto.LikeToggleRes;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class LikeService {
    private final LikeDomainService likeDomainService;

    public LikeToggleRes toggle(String postId, UUID userId) {
        return LikeToggleRes.from(likeDomainService.toggle(postId, userId));
    }
}
