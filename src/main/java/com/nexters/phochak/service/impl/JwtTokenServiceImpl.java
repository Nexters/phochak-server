package com.nexters.phochak.service.impl;

import com.auth0.jwt.JWT;
import com.nexters.phochak.config.property.JwtProperties;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.request.ReissueTokenRequestDto;
import com.nexters.phochak.dto.response.JwtResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.RefreshTokenRepository;
import com.nexters.phochak.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nexters.phochak.dto.TokenDto.TOKEN_TYPE;

@Slf4j
@Transactional
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final String secretKey;
    private final long accessTokenExpireLength;
    private final long refreshTokenExpireLength;

    public JwtTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                               JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secretKey = jwtProperties.getSecretKey();
        this.accessTokenExpireLength = jwtProperties.getAccessTokenExpireLength();
        this.refreshTokenExpireLength = jwtProperties.getRefreshTokenExpireLength();
    }

    @Override
    public JwtResponseDto issueToken(Long userId) {
        if (Objects.isNull(userId)) {
            throw new PhochakException(ResCode.NOT_FOUND_USER);
        }
        TokenDto accessToken = generateToken(userId, accessTokenExpireLength);
        TokenDto refreshToken = generateToken(userId, refreshTokenExpireLength);

        refreshTokenRepository.saveWithAccessToken(refreshToken.getTokenString(), accessToken.getTokenString());

        return JwtResponseDto.builder()
                .accessToken(createTokenStringForResponse(accessToken))
                .expiresIn(accessToken.getExpiresIn())
                .refreshToken(createTokenStringForResponse(refreshToken))
                .refreshTokenExpiresIn(refreshToken.getExpiresIn())
                .build();
    }

    @Override
    public Long validateJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.valueOf((String) claims.get("userId"));
        } catch (ExpiredJwtException e) {
            log.info("JwtTokenServiceImpl|Token expired: {}", token, e);
            throw new PhochakException(ResCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            log.warn("JwtTokenServiceImpl|Token Exception: {}", token, e);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }
    }

    @Override
    public JwtResponseDto reissueToken(ReissueTokenRequestDto reissueTokenRequestDto) {
        String currentAccessToken = parseOnlyTokenFromRequest(reissueTokenRequestDto.getAccessToken());
        String currentRefreshToken = parseOnlyTokenFromRequest(reissueTokenRequestDto.getRefreshToken());

        //RT ??????
        Long userId = validateJwt(currentRefreshToken);

        //?????? ?????? ???????????? RT ?????? ?????? ??????
        //redis?????? RT??? ???????????? AT ????????? ?????? (?????? ??? return ????????? ??????)
        String accessToken = refreshTokenRepository.findAccessToken(currentRefreshToken);
        refreshTokenRepository.expire(currentRefreshToken);

        if(!currentAccessToken.equals(accessToken)) {
            log.warn("JwtTokenServiceImpl|RT and AT are not matched: RT({})", currentRefreshToken);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }

        //AT ?????? ???????????? ?????? -> RT ????????? ???????????? ?????? ??????
        if(!isAccessTokenExpired(currentAccessToken)) {
            log.warn("JwtTokenServiceImpl|Request reissue when AT was not expired: RT({})", currentRefreshToken);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }

        return issueToken(userId);
    }

    @Override
    public void logout(String refreshToken) {
        refreshToken = parseOnlyTokenFromRequest(refreshToken);
        validateJwt(refreshToken);
        refreshTokenRepository.expire(refreshToken);
    }

    @Override
    public TokenDto generateToken(Long userId, Long expireLength) {
        // header ??????
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // payload ??????
        Map<String, String> payloads = new HashMap<>();
        payloads.put("userId", String.valueOf(userId));

        Date ext = new Date();
        ext.setTime(ext.getTime() + expireLength);

        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setExpiration(ext)
                .signWith(key)
                .compact();

        return new TokenDto(jwt, String.valueOf(expireLength));
    }

    private boolean isAccessTokenExpired(String accessToken) {
        return JWT.decode(accessToken).getExpiresAt().before(new Date());
    }

    public static String parseOnlyTokenFromRequest(String token) {
        if (!token.startsWith(TOKEN_TYPE + " ")) {
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }
        return token.substring(TOKEN_TYPE.length()).trim();
    }

    private static String createTokenStringForResponse(TokenDto accessToken) {
        return TokenDto.TOKEN_TYPE + " " + accessToken.getTokenString();
    }

}
