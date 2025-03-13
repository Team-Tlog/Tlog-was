package com.se.Tlog.global.util.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key,Object value,long expiration){
        redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.MILLISECONDS);
    }
}
