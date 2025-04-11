package com.se.Tlog.config.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.custom.rabbitmq.chat")
public class RabbitMqChatProperties {
    private String queue;
    private String exchange;
    private String routingKey;
}
