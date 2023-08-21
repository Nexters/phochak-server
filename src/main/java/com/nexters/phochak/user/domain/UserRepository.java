package com.nexters.phochak.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuthProviderEnum provider, String providerId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
