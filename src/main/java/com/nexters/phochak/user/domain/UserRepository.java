package com.nexters.phochak.user.domain;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    default User getBy(Long userId) {
        return findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
    }
    Optional<User> findByProviderAndProviderId(OAuthProviderEnum provider, String providerId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
