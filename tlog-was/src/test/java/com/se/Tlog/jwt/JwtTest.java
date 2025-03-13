package com.se.Tlog.jwt;

import com.se.Tlog.domain.User.Entity.Role;
import com.se.Tlog.global.util.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class JwtTest {

    private JwtUtil jwtUtil;


    @BeforeEach
    void setup() {
        String secretKey = "GjCU1g8/AbJhSzj4McaCKa7Amu+Fz1N4w8jJfZanEIk="; // 테스트용 키

        jwtUtil = new JwtUtil(
                secretKey,
                Duration.ofDays(10000),
                Duration.ofDays(7),
                "issuer"
        );
    }

    @Test
    void generateTokenTest(){

        String accessToken = jwtUtil.generateAccessToken("hello", Role.USER.toString());
        String refreshToken = jwtUtil.generateRefreshToken("hello", Role.USER.toString());
        System.out.println("token = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
    }
}

