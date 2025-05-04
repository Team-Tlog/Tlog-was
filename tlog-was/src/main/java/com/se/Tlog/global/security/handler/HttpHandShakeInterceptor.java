package com.se.Tlog.global.security.handler;

import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class HttpHandShakeInterceptor implements HandshakeInterceptor {

    private final AccessTokenProvider accessTokenProvider;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authorization = request.getHeaders().get("Authorization").get(0);  //현재 브라우저로 테스트를 하고있어서 헤더를 체울수가 없음 그래서 주석처리! 앱과 통신할땐 해당 코드 사용할 것!

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("TOKEN NOT FOUND or TOKEN HEADER IS WRONG : " + authorization);
            return false;
        }
        try {
            String token = authorization.split(" ")[1];

            //token = token.split(" ")[1];
            System.out.println("token = " + token);
            Claims claims = accessTokenProvider.parseAndValidate(token);
            System.out.println("claims = " + claims);
            CustomPrincipal userPrincipal = CustomPrincipal.from(claims);
            attributes.put("userPrincipal", userPrincipal);

        } catch (Exception e) {
            log.warn("JWT 인증 실패: {}", e.getMessage());
            return false;
        }

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        String clientId = servletRequest.getServletRequest().getRemoteAddr();
        attributes.put("clientId", clientId);   //세션 속성에 저장

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
