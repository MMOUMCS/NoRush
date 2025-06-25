package com.example.norush.util;

import static com.example.norush.exception.AuthExceptionType.EXPIRED_TOKEN;
import static com.example.norush.exception.AuthExceptionType.INVALID_SIGNATURE;
import static com.example.norush.exception.AuthExceptionType.INVALID_TOKEN;
import static com.example.norush.exception.AuthExceptionType.MALFORMED_TOKEN;
import static com.example.norush.exception.AuthExceptionType.SIGNATURE_NOT_FOUND;
import static com.example.norush.exception.AuthExceptionType.UNSUPPORTED_TOKEN;

import com.example.norush.domain.Member;
import com.example.norush.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;

import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("JwtUtil 생성자 호출됨");

        try {
            if (secret == null || secret.length() < 32) {
                throw new IllegalArgumentException("secret은 최소 32자 이상이어야 합니다.");
            }

            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            System.out.println("secretKey 초기화 성공");

        } catch (Exception e) {
            System.out.println("JwtUtil 초기화 중 예외 발생: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    public String createAccessToken(Member member) {
        return Jwts.builder()
                .claim("id", member.getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Member member) {
        return Jwts.builder()
                .claim("id", member.getId())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        if (isTokenExpired(refreshToken)) {
            return false;
        }
        try {
            getClaimFromToken(refreshToken);
            return true;
        } catch (NullPointerException | JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getClaimFromToken(token).getExpiration().before(new Date());
    }

    public String getMemberIdFromToken(String token) {
        try {
            return getClaimFromToken(token).get("id", String.class);
        } catch (SecurityException e) {
            throw new AuthException(SIGNATURE_NOT_FOUND);
        } catch (MalformedJwtException e) {
            throw new AuthException(MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new AuthException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new AuthException(INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new AuthException(INVALID_SIGNATURE);
        }
    }

    private Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
