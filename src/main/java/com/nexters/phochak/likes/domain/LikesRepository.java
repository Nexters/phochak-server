package com.nexters.phochak.likes.domain;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long>, LikesCustomRepository {
    boolean existsByUserAndPost(UserEntity user, Post post);
    Optional<Likes> findByUserAndPost(UserEntity user, Post post);
}
