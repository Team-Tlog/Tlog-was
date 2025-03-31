package com.se.Tlog.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final String issuer;

    public JwtUtil(
            @Value("${JWT_SECRET_KEY}") String secretKey,
            @Value("${JWT_ISSUER}") String issuer
    ){
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.issuer = issuer;
    }

    public String generateToken(String subject,Duration duration, String role, Map<String, Object> claims){
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .issuer(issuer)
                .expiration(createExpire(duration.toMillis()))
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)   //jwt 토큰 전체
                .getPayload();
    }
    private Date createExpire(Long expiration){
        return new Date(System.currentTimeMillis() + expiration);
    }
}
