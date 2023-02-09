package com.nexters.phochak.repository;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuthProviderEnum provider, String providerId);

    Optional<User> findByNickname(String nickname);

    Boolean existsByNickname(String nickname);
}
