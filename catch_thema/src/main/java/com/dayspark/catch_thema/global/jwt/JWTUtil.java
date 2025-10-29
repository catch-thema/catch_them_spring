package com.dayspark.catch_thema.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // (옵션) 단순 subject/role 토큰 생성이 필요할 경우 사용할 수 있음
    public String createToken(String userId, String role, long expiredSec) {
        long expiredMs = expiredSec * 1000L;
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiredMs);
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    // DevView 스타일: user PK, username, role을 담는 createJwt(expiredSec)
    public String createJwt(Long userId, String email, String role, long expiredSec) {
        long expiredMs = expiredSec * 1000L;
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expiredMs);
        return Jwts.builder()
                .claim("id", userId)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    public boolean validate(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) { return getAllClaims(token).getSubject(); }

    public String getRole(String token) { Object v = getAllClaims(token).get("role"); return v == null ? null : v.toString(); }

    // DevView 스타일 헬퍼들
    public Long getId(String token) {
        return getAllClaims(token).get("id", Long.class);
    }

    public String getEmail(String token) {
        return getAllClaims(token).get("email", String.class);
    }

    public Boolean isExpired(String token) {
        Date exp = getAllClaims(token).getExpiration();
        return exp.before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


