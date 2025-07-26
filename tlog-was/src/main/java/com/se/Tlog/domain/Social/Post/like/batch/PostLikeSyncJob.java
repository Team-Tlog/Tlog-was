package com.se.Tlog.domain.Social.Post.like.batch;

import com.se.Tlog.domain.Social.Post.domain.service.PostDomainService;
import com.se.Tlog.domain.Social.Post.like.infra.LikeCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostLikeSyncJob {

    private final LikeCacheRepository likeCacheRepository;
    private final PostDomainService postDomainService;
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 10 * 60 * 1000) // 10분마다
    public void syncLikeCountsToMongo() {
        Set<String> dirtyPostIds = redisTemplate.opsForSet().members("post:dirty:ids");

        if(dirtyPostIds == null || dirtyPostIds.isEmpty()) return;

        for (String postId : dirtyPostIds) {
            try {
                int postRedisCount = likeCacheRepository.getLikeCountWithFallback(postId);
                postDomainService.updateLikeCount(postId, postRedisCount);
                likeCacheRepository.removeDirtyIdsPostId(postId);
                log.info("Syncing like counts for {} posts", dirtyPostIds.size());
            } catch (Exception e) {
                log.error("Fail to sync like count for postId = {}", postId, e);
            }
        }
    }
}
