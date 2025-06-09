package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitConsumer {

    private final ChatConsumerService chatConsumerService;

    @RabbitListener(queues = "${spring.custom.rabbitmq.chat.queue}")
    public void receiveMessage(ChatMessageRequestDto chatMessageRequestDto) {
        log.info("Received Message: {}", chatMessageRequestDto);

        chatConsumerService.receivedChatMessage(chatMessageRequestDto);
    }
}
