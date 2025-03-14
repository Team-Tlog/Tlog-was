package com.se.Tlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.se.Tlog.domain.**.infrastructure.jpa"
)
@EnableMongoRepositories(
        basePackages = "com.se.Tlog.domain.**.infrastructure.mongo"
)
public class DataSourceConfig {
}
