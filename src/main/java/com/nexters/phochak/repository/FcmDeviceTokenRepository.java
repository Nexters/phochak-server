package com.nexters.phochak.repository;

import com.nexters.phochak.domain.FcmDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmDeviceTokenRepository extends JpaRepository<FcmDeviceToken, Long> {
}
