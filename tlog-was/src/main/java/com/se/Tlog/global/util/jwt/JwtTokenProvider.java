package com.se.Tlog.global.util.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public interface JwtTokenProvider {

    boolean isTokenExpired(String token);

    Claims parseToken(String token);

    Claims parseAndValidate(String token);

}
