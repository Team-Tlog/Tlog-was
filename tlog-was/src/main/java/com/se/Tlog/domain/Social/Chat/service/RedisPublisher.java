package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    // 채팅방에 입장하여 메시지를 작성하면 해당 메시지를 Redis Topic에 발행하는 기능
    public void publish(ChannelTopic topic, ChatMessage message) {
        System.out.println("topic = " + topic);
        System.out.println("message = " + message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
