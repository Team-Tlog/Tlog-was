package com.se.Tlog.jwt;

import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.JwtUtil;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class JwtTest {

    private JwtUtil jwtUtil;
    private AccessTokenProvider accessTokenProvider;
    private RefreshTokenProvider refreshTokenProvider;


    @BeforeEach
    void setup() {
        String secretKey = "GjCU1g8/AbJhSzj4McaCKa7Amu+Fz1N4w8jJfZanEIk="; // 테스트용 키

        jwtUtil = new JwtUtil(
                secretKey,
                "issuer"
        );
    }

    @Test
    void generateTokenTest(){

        String accessToken = accessTokenProvider.generateToken("hello", Role.USER.toString());
        String refreshToken = refreshTokenProvider.generateToken("hello", Role.USER.toString());
        System.out.println("token = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
    }
}

