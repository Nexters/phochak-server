package com.nexters.phochak.post.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long>, HashtagCustomRepository {

    @Query("delete from HashtagEntity h where h.post.id = :postId")
    @Modifying
    void deleteAllByPostId(@Param("postId") Long postId);

    @Query("delete from HashtagEntity h where h.post.id IN (:postIdList)")
    @Modifying
    void deleteAllByPostIdIn(@Param("postIdList") List<Long> postIdList);

    @Query("select distinct h.tag from HashtagEntity h where h.tag like :hashtag% order by length(h.tag)")
    List<String> findByHashtagStartsWith(@Param("hashtag") String hashtag, Pageable pageable);

    boolean existsByTag(String tag);
}
