package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Optional<Shorts> findByKey(String key);
}
