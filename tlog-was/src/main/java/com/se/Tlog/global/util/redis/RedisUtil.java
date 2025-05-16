package com.se.Tlog.global.util.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.se.Tlog.global.util.redis.RedisProperties.PENDING_TAGGING_DESTINATION;

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
    
    /**
     * TaggingQueue는 새로 추가되었으나 태그처리가 안된 여행지를 저장합니다.
     * <br>그러나 관리자 검수가 안된 것을 저장하는 용도로도 사용하고 있습니다.
     * @param destinationId
     */
    public void pushDestinationIdToTaggingQueue(String destinationId) {
        redisTemplate.opsForSet().add(PENDING_TAGGING_DESTINATION, destinationId);
    }
    /**
     * TaggingQueue는 새로 추가되었으나 태그처리가 안된 여행지를 저장합니다.
     * <br>그러나 관리자 검수가 안된 것을 저장하는 용도로도 사용하고 있습니다.
     * @return
     */
    public String popDestinationIdFromTaggingQueue() {
        return (String) redisTemplate.opsForSet().pop(PENDING_TAGGING_DESTINATION);
    }
    /**
     * TaggingQueue에서 주어진 destinationId가 있으면 제거합니다.
     * <br>
     * <br>TaggingQueue는 새로 추가되었으나 태그처리가 안된 여행지를 저장합니다.
     * <br>그러나 관리자 검수가 안된 것을 저장하는 용도로도 사용하고 있습니다.
     * @return 제거에 성공하면 해당 destinationId를 반환하며,
     * <br> 없을 경우 null을 반환합니다.
     */
    public Optional<String> removeDestinationIdFromTaggingQueue(String destinationId) {
        if (1 == redisTemplate.opsForSet().remove(PENDING_TAGGING_DESTINATION, destinationId))
            return Optional.of(destinationId);
        else
            return Optional.ofNullable((String)null);
    }
    /**
     * TaggingQueue는 새로 추가되었으나 태그처리가 안된 여행지를 저장합니다.
     * <br>그러나 관리자 검수가 안된 것을 저장하는 용도로도 사용하고 있습니다.
     * @return
     */
    public List<String> getAllDestinationIdFromTaggingQueue() {
        return redisTemplate.opsForSet().members(PENDING_TAGGING_DESTINATION)
        .stream().map(desId -> (String)desId).collect(Collectors.toList());
    }
    /**
     * TaggingQueue는 새로 추가되었으나 태그처리가 안된 여행지를 저장합니다.
     * <br>그러나 관리자 검수가 안된 것을 저장하는 용도로도 사용하고 있습니다.
     * @param destinationId
     * @return
     */
    public boolean isInTaggingQueue(String destinationId) {
        return !redisTemplate.opsForSet().isMember(PENDING_TAGGING_DESTINATION, destinationId);
    }
}
