package com.nexters.phochak.user.adapter.adapter.out.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.application.port.in.AppleUserInformation;
import com.nexters.phochak.user.application.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.application.application.port.out.OAuthRequestPort;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleOAuthRequestAdapter implements OAuthRequestPort {
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

    @Getter
    public static class Keys {
        @JsonProperty(value = "keys")
        private List<Key> keyList;

        @Getter
        public static class Key {
            private String kty;
            private String kid;
            private String use;
            private String alg;
            private String n;
            private String e;
        }
    }

}
