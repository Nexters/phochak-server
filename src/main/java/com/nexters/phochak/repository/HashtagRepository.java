package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagCustomRepository {

    @Query("delete from Hashtag h where h.post.id = :postId")
    @Modifying
    void deleteAllByPostId(@Param("postId") Long postId);

    @Query("delete from Hashtag h where h.post.id IN (:postIdList)")
    @Modifying
    void deleteAllByPostIdIn(@Param("postIdList") List<Long> postIdList);

    @Query("select h from Hashtag h where h.tag like :hashtag%")
    List<String> findByHashtagStartsWith(@Param("hashtag") String hashtag, Pageable pageable);

}
