package com.se.Tlog.domain.Search.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PopularityCacheRepository {
    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "popular:destination:";
    private static final String ALL_IDS_KEY = "popular:destination:all";

    public void addScore(String destinationId, int score) {
        redisTemplate.opsForValue().increment(KEY_PREFIX + destinationId, score);
        redisTemplate.opsForSet().add(ALL_IDS_KEY, destinationId);
    }

    private long removeScore(String destinationId) {
        String value = redisTemplate.opsForValue().getAndDelete(KEY_PREFIX + destinationId);
        redisTemplate.opsForSet().remove(ALL_IDS_KEY, destinationId);
        return (value != null ? Long.parseLong(value) : 0);
    }

    public Map<String, Long> popAllCachedScore() {
        Set<String> ids = redisTemplate.opsForSet().members(ALL_IDS_KEY);
        if (ids == null)
            return Map.of();
        return ids.stream().collect(Collectors.toMap(Function.identity(), this::removeScore));
    }
}
