package com.nexters.phochak.service.impl;

import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final SecretKey secretKey;
    private final long expireLength;

    public JwtTokenServiceImpl(@Value("${security.jwt.token.secret-key}") String secretKey,
                               @Value("${security.jwt.token.expire-length}") long expireLength) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expireLength = expireLength;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public TokenDto generateAccessToken(Long userId) {
        // header 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        // payload 설정
        Map<String, String> payloads = new HashMap<>();
        payloads.put("userId", String.valueOf(userId));

        Date ext = new Date();
        ext.setTime(ext.getTime() + expireLength);

        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setExpiration(ext)
                .signWith(this.secretKey)
                .compact();

        return new TokenDto(jwt, String.valueOf(expireLength));
    }
}
