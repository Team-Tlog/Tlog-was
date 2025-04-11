package com.se.Tlog.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitQueueConfig {

    private final RabbitMqChatProperties rabbitMqChatProperties;

    @Bean
    public Queue queue() {
        return new Queue(rabbitMqChatProperties.getQueue(), true, false, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMqChatProperties.getExchange());
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(rabbitMqChatProperties.getRoutingKey());
    }
}
