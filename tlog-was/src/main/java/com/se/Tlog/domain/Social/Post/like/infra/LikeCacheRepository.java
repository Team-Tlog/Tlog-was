package com.se.Tlog.domain.Social.Post.like.infra;

import com.se.Tlog.domain.Social.Post.domain.service.PostDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class LikeCacheRepository {
    private final StringRedisTemplate redisTemplate;
    private final PostDomainService postDomainService;

    private static final Duration LIKE_COUNT_TTL = Duration.ofMinutes(15);

    private String likeSetKey(String postId) {
        return "post:" + postId + ":likes";
    }
    private String likeCountKey(String postId) {
        return "post:" + postId + ":likeCount";
    }

    public void addLike(String postId, UUID userId) {
        redisTemplate.opsForSet().add(likeSetKey(postId), userId.toString());
    }

    public void removeLike(String postId, UUID userId) {
        redisTemplate.opsForSet().remove(likeSetKey(postId), userId.toString());
    }

    public boolean isAlreadyLiked(String postId, UUID userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeSetKey(postId), userId.toString()));
    }

    public void incrementLikeCount(String postId) {
        String key = likeCountKey(postId);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, LIKE_COUNT_TTL);
        redisTemplate.opsForSet().add("post:dirty:ids", postId);
    }

    public void decrementLikeCount(String postId) {
        String key = likeCountKey(postId);
        redisTemplate.opsForValue().decrement(key);
        redisTemplate.expire(key, LIKE_COUNT_TTL);
        redisTemplate.opsForSet().add("post:dirty:ids", postId);
    }

    public int getLikeCountWithFallback(String postId) {
        String key = likeCountKey(postId);
        String count = redisTemplate.opsForValue().get(key);

        if(count != null) return Integer.parseInt(count); // 캐시에 게시글 존재

        // fallback - DB에서 조회
        int dbCount = postDomainService.findByIdOrThrow(postId).getLikeCount();

        redisTemplate.opsForValue().set(key, String.valueOf(dbCount), LIKE_COUNT_TTL);

        return dbCount;
    }

    public void removeDirtyIdsPostId(String postId) {
        redisTemplate.opsForSet().remove("post:dirty:ids", postId);
    }
}
