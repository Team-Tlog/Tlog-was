package com.se.Tlog;

import com.se.Tlog.config.rabbitmq.RabbitMqChatProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableConfigurationProperties(RabbitMqChatProperties.class)
@EnableMongoAuditing
@EnableJpaAuditing
public class TlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlogApplication.class, args);
	}

}
