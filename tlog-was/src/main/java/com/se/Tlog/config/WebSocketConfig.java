package com.se.Tlog.config;


import com.se.Tlog.global.security.handler.HttpHandShakeInterceptor;
import com.se.Tlog.global.security.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private final HttpHandShakeInterceptor httpHandShakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("웹 소켓 endpoint registered"); // /ws-stomp를 웹 소켓 엔드포인트로 등록하여 통신 연결한다.
        registry.addEndpoint("/ws-stomp")
                .addInterceptors(httpHandShakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
