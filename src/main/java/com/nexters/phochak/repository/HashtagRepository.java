package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("delete from Hashtag h where h.post.id=:id")
    @Modifying
    void deleteAllByPostId(Long id);
}
