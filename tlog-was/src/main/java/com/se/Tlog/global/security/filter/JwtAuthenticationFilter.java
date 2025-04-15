package com.se.Tlog.global.security.filter;

import com.se.Tlog.domain.Admin.repository.jpa.AdminRepository;
import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.security.dto.AdminDetails;
import com.se.Tlog.global.security.dto.AppUserDetails;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.redis.RedisProperties;
import com.se.Tlog.global.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("TOKEN NOT FOUND or TOKEN HEADER IS WRONG : " + authorization);
        } else {
            String accessToken = authorization.split(" ")[1];

            Claims claims = accessTokenProvider.parseToken(accessToken);
            String jti = claims.get("jti").toString();
            String accessKey = RedisProperties.ACCESS_TOKEN_PREFIX + jti;

            boolean isBlacklisted = redisUtil.isTokenBlacklisted(accessKey);

            if (isBlacklisted) {
                log.warn("BLOCKED TOKEN (jti = {})", jti);
                response.setStatus(ErrorType.UN_AUTHORIZATION.getStatusCode());
                response.setContentType("application/json");
                response.getWriter().write("Token is blacklisted.");
                return;
            }
            Authentication authToken = getAuthentication(claims, response);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request,response);
    }

    private Authentication getAuthentication(Claims claims, HttpServletResponse response) throws IOException {

        UUID id = UUID.fromString(claims.getSubject());
        String role = claims.get("role").toString();

        UserDetails userDetails = null;

        if(role.equals(Role.USER.getValue())){
            userDetails = userRepository.findById(id)
                    .map(AppUserDetails::new)
                    .orElseGet(() -> {
                        log.error(ErrorType.USER_NOT_FOUND.getMessage());
                        return null;
                    });
        } else if(role.equals(Role.ADMIN.getValue())){
            userDetails = adminRepository.findById(id)
                    .map(AdminDetails::new)
                    .orElseGet(() -> {
                        log.error(ErrorType.ADMIN_NOT_FOUND.getMessage());
                        return null;
                    });
        }

        return userDetails == null ? null : new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
