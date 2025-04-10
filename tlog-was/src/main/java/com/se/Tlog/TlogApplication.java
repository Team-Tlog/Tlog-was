package com.se.Tlog;

import com.se.Tlog.config.RabbitMqChatProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RabbitMqChatProperties.class)
public class TlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlogApplication.class, args);
	}

}
