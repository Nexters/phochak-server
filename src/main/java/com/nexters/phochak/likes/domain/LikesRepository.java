package com.nexters.phochak.likes.domain;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long>, LikesCustomRepository {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Likes> findByUserAndPost(User user, Post post);
}
