package com.nexters.phochak.repository;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByProviderAndProviderId(OAuthProviderEnum provider, String providerId);
}
