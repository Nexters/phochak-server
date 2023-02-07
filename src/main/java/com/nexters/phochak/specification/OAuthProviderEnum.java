package com.nexters.phochak.specification;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import lombok.Getter;

@Getter
public enum OAuthProviderEnum {
    /**
     * 지원하는 OAuth Provider 목록
     */
    APPLE("apple"), KAKAO("kakao"), NAVER("naver"), KAKAO_TEST("kakao_test");

    private final String code;

    OAuthProviderEnum(String code) {
        this.code = code;
    }

    public static OAuthProviderEnum codeOf(String code) {
        for (OAuthProviderEnum target : OAuthProviderEnum.values()) {
            if (target.getCode().equals(code)) {
                return target;
            }
        }

        throw new PhochakException(ResCode.INVALID_INPUT, "지원하지 않는 provider code입니다.");
    }
}
