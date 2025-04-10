package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.config.RabbitMqChatProperties;
import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabbitPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqChatProperties rabbitMqChatProperties;


    public void publish(String routingKey, ChatMessage message) {
        System.out.println("routingKey = " + routingKey);
        System.out.println("message = " + message);
        rabbitTemplate.convertAndSend(rabbitMqChatProperties.getExchange(), routingKey, message);
    }
}
