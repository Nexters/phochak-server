package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    default UserEntity getBy(Long userId) {
        return findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
    }
    Optional<UserEntity> findByProviderAndProviderId(OAuthProviderEnum provider, String providerId);

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    void updateNicknameById(Long id, String nickname);
}
