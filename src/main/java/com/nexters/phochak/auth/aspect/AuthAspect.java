package com.nexters.phochak.auth.aspect;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AuthAspect {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final HttpServletRequest httpServletRequest;
    private final UserService userService;

    @Around("@annotation(com.nexters.phochak.auth.annotation.Auth)")
    public Object validateAccessToken(final ProceedingJoinPoint joinPoint) throws Throwable {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            throw new PhochakException(ResCode.TOKEN_NOT_FOUND);
        }

        Long userId = null;
        try {
            userId = jwtTokenService.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.info("AuthAspect|Token expired: {}", accessToken, e);
            throw new PhochakException(ResCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            log.error("AuthAspect|Token Exception: {}", accessToken, e);
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }

        // 유저 검증 후 Thread Local 변수에 할당
        try {
            userService.validateUser(userId);
        } catch (PhochakException e) {
            log.warn("AuthAspect|Invalid User: {}", userId, e);
        }
        UserContext.CONTEXT.set(userId);

        return joinPoint.proceed();
    }
}
