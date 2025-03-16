package com.se.Tlog.config;

import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.infrastructure.api.SsoOauthManager;
import com.se.Tlog.domain.User.infrastructure.api.SsoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class SsoServiceConfig {
    @Bean
    public Map<SsoType, SsoService> ssoServiceMap(List<SsoService> ssoServices) {
        return ssoServices.stream()
                .collect(Collectors.toMap(SsoService::getType, Function.identity()));
    }

    @Bean
    public Map<SsoType, SsoOauthManager> ssoOauthManagerMap(List<SsoOauthManager> ssoOauthManagers) {
        return ssoOauthManagers.stream()
                .collect(Collectors.toMap(SsoOauthManager::getType, Function.identity()));
    }
}
