package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Optional<Shorts> findByUploadKey(String uploadKey);

    @Modifying
    @Query("DELETE FROM Shorts s WHERE s.uploadKey IN (:shortsKeyList)")
    void deleteAllByUploadKeyIn(@Param("shortsKeyList") List<String> shortsKeyList);
}
