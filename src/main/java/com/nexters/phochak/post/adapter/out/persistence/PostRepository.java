package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.shorts.adapter.out.persistence.ShortsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long>, PostCustomRepository {
    Optional<PostEntity> findByShorts(ShortsEntity shortsEntity);

    @Query("select p from PostEntity p left join fetch p.shorts where p.id = :postId")
    Optional<PostEntity> findPostFetchJoin(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.view = p.view + 1 WHERE p.id = :postId")
    int updateView(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE from PostEntity p WHERE p.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("select p from PostEntity p left join fetch p.shorts where p.user.id = :userId")
    List<PostEntity> findAllPostByUserIdFetchJoin(@Param("userId") Long userId);
}
