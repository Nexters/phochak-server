package com.nexters.phochak.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.client.AppleAuthKeyFeignClient;
import com.nexters.phochak.dto.AppleUserInformation;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.dto.jwt.Keys;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleOAuthServiceImpl implements OAuthService {
    private static final OAuthProviderEnum OAUTH_PROVIDER = OAuthProviderEnum.APPLE;
    private final AppleAuthKeyFeignClient appleAuthKeyFeignClient;

    @Override
    public OAuthProviderEnum getOAuthProvider() {
        return OAUTH_PROVIDER;
    }

    @Override
    public OAuthUserInformation requestUserInformation(String token) {
        // jwt 복호화용 공개키 목록 가져오기
        String publicKeys = appleAuthKeyFeignClient.call();

        ObjectMapper objectMapper = new ObjectMapper();
        Keys keys;
        try {
            keys = objectMapper.readValue(publicKeys, Keys.class);

            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean isVerified = isVerifiedToken(keys, signedJWT);

            if (isVerified) {
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

                return AppleUserInformation.builder()
                        .providerId(jwtClaimsSet.getSubject())
                        .build();
            } else {
                log.warn("AppleOAuthServiceImpl|requestUserInformation 서명 검증 실패 : {}", token);
                throw new PhochakException(ResCode.INVALID_APPLE_TOKEN);
            }

        } catch (JsonProcessingException | ParseException | JOSEException e) {
            log.error("AppleOAuthServiceImpl|requestUserInformation Exception: {}", token, e);
            throw new PhochakException(ResCode.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isVerifiedToken(Keys keys, SignedJWT signedJWT) throws ParseException, JsonProcessingException, JOSEException {
        boolean isVerified = false;
        ObjectMapper objectMapper = new ObjectMapper();

        for (Keys.Key key : keys.getKeyList()) {
            RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
            RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            if (signedJWT.verify(verifier)) {
                isVerified = true;
                break;
            }
        }
        return isVerified;
    }
}
