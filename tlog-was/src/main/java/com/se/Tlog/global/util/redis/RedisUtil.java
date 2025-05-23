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

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public void setBlacklistToken(String key, long remainingTime) {
        redisTemplate.opsForValue().set(key, "blacklisted", remainingTime, TimeUnit.MILLISECONDS);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public boolean isTokenBlacklisted(String key) {
        if ("blacklisted".equals(get(key))) {
            return true;
        }else{
            return false;
        }
    }
}
