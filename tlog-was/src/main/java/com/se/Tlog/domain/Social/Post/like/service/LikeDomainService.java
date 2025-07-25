package com.se.Tlog.domain.Social.Post.like.service;

import com.se.Tlog.domain.Social.Post.like.infra.LikeCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeDomainService {

    private final LikeCacheRepository likeCacheRepository;

    public boolean toggle(String postId, UUID userId) {
        if (likeCacheRepository.isAlreadyLiked(postId, userId)) {
            unlike(postId,userId);
            return false;
        }else {
            like(postId, userId);
            return true;
        }
    }

    private void like(String postId, UUID userId) {
        likeCacheRepository.addLike(postId, userId);
        likeCacheRepository.incrementLikeCount(postId);
    }

    private void unlike(String postId, UUID userId) {
        likeCacheRepository.removeLike(postId, userId);
        likeCacheRepository.decrementLikeCount(postId);
    }
}
