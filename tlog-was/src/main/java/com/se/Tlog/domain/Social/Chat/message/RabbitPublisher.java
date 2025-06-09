package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.config.rabbitmq.RabbitMqChatProperties;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabbitPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqChatProperties rabbitMqChatProperties;


    public void publish(String routingKey, ChatMessageRequestDto chatMessageRequestDto) {
        System.out.println("routingKey = " + routingKey);
        System.out.println("message = " + chatMessageRequestDto);
        rabbitTemplate.convertAndSend(rabbitMqChatProperties.getExchange(), routingKey, chatMessageRequestDto);
    }
}
