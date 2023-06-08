package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUser, Long> {
}