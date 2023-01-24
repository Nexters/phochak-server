package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.RefreshToken;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.response.LoginResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.RefreshTokenRepository;
import com.nexters.phochak.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Transactional
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecretKey secretKey;
    private final long accessTokenExpireLength;
    private final long refreshTokenExpireLength;

    public JwtTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                               @Value("${security.jwt.token.secret-key}") String secretKey,
                               @Value("${security.jwt.token.access-token-expire-length}") long accessTokenExpireLength,
                               @Value("${security.jwt.token.refresh-token-expire-length}") long refreshTokenExpireLength) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireLength = accessTokenExpireLength;
        this.refreshTokenExpireLength = refreshTokenExpireLength;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public LoginResponseDto createLoginResponse(Long userId) {
        if (Objects.isNull(userId)) {
            throw new PhochakException(ResCode.NOT_FOUND_USER);
        }
        TokenDto accessToken = generateToken(userId, accessTokenExpireLength);
        TokenDto refreshToken = generateToken(userId, refreshTokenExpireLength);

        // 해당 유저 id 앞으로 refresh token 저장
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(userId)
                .refreshTokenString(refreshToken.getTokenString())
                .build());

        return LoginResponseDto.builder()
                .tokenType(TokenDto.TOKEN_TYPE)
                .accessToken(accessToken.getTokenString())
                .expiresIn(accessToken.getExpiresIn())
                .refreshToken(refreshToken.getTokenString())
                .refreshTokenExpiresIn(refreshToken.getExpiresIn())
                .build();
    }

    private TokenDto generateToken(Long userId, Long expireLength) {
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
