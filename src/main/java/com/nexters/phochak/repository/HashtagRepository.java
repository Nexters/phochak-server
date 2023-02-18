package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("delete from Hashtag h where h.post.id = :postId")
    @Modifying
    void deleteAllByPostId(@Param("postId") Long postId);
}
