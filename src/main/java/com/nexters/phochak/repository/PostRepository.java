package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    Optional<Post> findByShorts(Shorts shorts);

    @Query("select p from Post p left join fetch p.shorts where p.id = :postId")
    Optional<Post> findPostFetchJoin(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.view = p.view + 1 WHERE p.id = :postId")
    int updateView(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE from Post p WHERE p.user = :user")
    void deleteAllByUser(@Param("user") User user);

    @Query("select p from Post p left join fetch p.shorts where p.user = :user")
    List<Post> findAllPostByUserFetchJoin(@Param("user") User user);
}
