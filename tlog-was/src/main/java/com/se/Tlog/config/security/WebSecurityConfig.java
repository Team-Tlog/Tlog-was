package com.se.Tlog.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.Tlog.domain.Admin.repository.jpa.AdminRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.security.filter.JwtAuthenticationFilter;
import com.se.Tlog.global.security.filter.JwtExceptionFilter;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.redis.RedisTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final AccessTokenProvider accessTokenProvider;
    private final ObjectMapper objectMapper;
    private final RedisTokenUtil redisTokenUtil;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository, AdminRepository adminRepository) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)


                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest ->{
                    authorizeHttpRequest
                            //.requestMatchers("/api/admin/**").hasRole(Role.ADMIN.toString()) // Admin 전용 역할 제한 필요!!
                            .requestMatchers("/test/**").permitAll();
                    log.error("서비스 API접근시 JWT 관련 Role 구분이 이루어지지 않았습니다!");



                    authorizeHttpRequest
                            .anyRequest().permitAll();


                })
                .addFilterBefore(new JwtAuthenticationFilter(accessTokenProvider,userRepository,adminRepository,redisTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);

        return http.build();
    }
}
