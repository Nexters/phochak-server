package com.nexters.phochak.deprecated.repository;

import com.nexters.phochak.auth.domain.RefreshTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RedisRefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Redis 데이터 저장 및 조회")
    void setAndGetData() {
        String AT = "access";
        String RT = "refresh";
        refreshTokenRepository.saveWithAccessToken(RT, AT);
        Assertions.assertThat(refreshTokenRepository.findAccessToken(RT)).isEqualTo(AT);
    }

    @Test
    @DisplayName("Redis 데이터 저장 및 삭제")
    void setAndDelete() {
        String AT = "access";
        String RT = "refresh";
        refreshTokenRepository.saveWithAccessToken(RT, AT);
        Assertions.assertThat(refreshTokenRepository.expire(RT)).isTrue();
        Assertions.assertThat(refreshTokenRepository.findAccessToken(RT)).isNull();
    }

    @Test
    @DisplayName("Redis 에 존재하지 않는 데이터를 삭제 시도하면, false를 반환한다")
    void deleteNotExistData() {
        Assertions.assertThat(refreshTokenRepository.expire("something")).isFalse();
    }
}