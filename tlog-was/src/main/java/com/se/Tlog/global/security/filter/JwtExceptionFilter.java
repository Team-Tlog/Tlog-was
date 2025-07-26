package com.se.Tlog.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.error.ErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("token expired");
            writeErrorResponse(response, ErrorType.TOKEN_EXPIRED, e.getMessage());

        } catch (JwtException e) {
            log.error("token authentication failed : " + e.getMessage());
            writeErrorResponse(response, ErrorType.UN_AUTHENTICATION, e.getMessage());

        }
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorType errorType, String message) throws IOException {
        response.setStatus(errorType.getStatusCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorRes errorRes = ErrorRes.of(errorType.getStatusCode(), message);
        response.getWriter().write(objectMapper.writeValueAsString(errorRes));
    }
}
