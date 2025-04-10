package com.se.Tlog.config.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.se.Tlog.domain.**.repository.jpa"
)
@EnableMongoRepositories(
        basePackages = "com.se.Tlog.domain.**.repository.mongo"
)
public class DataSourceConfig {
}
