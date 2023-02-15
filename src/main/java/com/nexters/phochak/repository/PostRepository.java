package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    Optional<Post> findByShorts(Shorts shorts);

    @Query("select distinct p from Post p left join fetch p.shorts where p.id = :postId")
    Optional<Post> findPostFetchJoin(Long postId);
}
