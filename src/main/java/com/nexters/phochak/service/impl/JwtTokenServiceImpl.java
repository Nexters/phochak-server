package com.nexters.phochak.service.impl;

import com.auth0.jwt.JWT;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.request.ReissueAccessTokenRequestDto;
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
import org.springframework.beans.factory.annotation.Value;
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
                               @Value("${security.jwt.token.secret-key}") String secretKey,
                               @Value("${security.jwt.token.access-token-expire-length}") long accessTokenExpireLength,
                               @Value("${security.jwt.token.refresh-token-expire-length}") long refreshTokenExpireLength) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secretKey = secretKey;
        this.accessTokenExpireLength = accessTokenExpireLength;
        this.refreshTokenExpireLength = refreshTokenExpireLength;
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
            log.error("JwtTokenServiceImpl|Token Exception: {}", token, e);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }
    }

    @Override
    public JwtResponseDto reissueToken(ReissueAccessTokenRequestDto reissueAccessTokenRequestDto) {
        String currentAccessToken = parseOnlyTokenFromRequest(reissueAccessTokenRequestDto.getAccessToken());
        String currentRefreshToken = parseOnlyTokenFromRequest(reissueAccessTokenRequestDto.getRefreshToken());

        //RT 검증
        Long userId = validateJwt(currentRefreshToken);

        //해당 요청 들어오면 RT 항상 만료 시킴
        //redis에서 RT과 매칭되는 AT 있는지 확인 (삭제 후 return 결과로 판단)
        boolean isTokenMatched = refreshTokenRepository.expire(currentRefreshToken);

        if(!isTokenMatched) {
            log.error("JwtTokenServiceImpl|RT and AT are not matched: RT({})", currentRefreshToken);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }

        //AT 아직 유효기간 남음 -> RT 탈취로 판단하고 강제 만료
        if(isAccessTokenExpired(currentAccessToken)) {
            log.error("JwtTokenServiceImpl|Request reissue when AT was not expired: RT({})", currentRefreshToken);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }

        return issueToken(userId);
    }

    @Override
    public TokenDto generateToken(Long userId, Long expireLength) {
        // header 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // payload 설정
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

    private Boolean isAccessTokenExpired(String accessToken) {
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
