package com.se.Tlog.jwt;

import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import com.se.Tlog.global.util.jwt.JwtUtil;
import com.se.Tlog.global.util.jwt.RefreshTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootTest
public class JwtTest {

    @Autowired
    private AccessTokenProvider accessTokenProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;

	@TestConfiguration
	static class JwtTestConfig {
		private static String secretKey = "GjCU1g8/AbJhSzj4McaCKa7Amu+Fz1N4w8jJfZanEIk="; // 테스트용 키
		
		@Bean
		public static JwtUtil jwtUtil() {
			return new JwtUtil(
	                secretKey,
	                "issuer"
	        );
		}
	}

    @Test
    void generateTokenTest(){
        String accessToken = accessTokenProvider.generateToken("hello", Role.USER.toString(),"","");
        String refreshToken = refreshTokenProvider.generateToken("hello", Role.USER.toString());
        System.out.println("token = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
    }
}

