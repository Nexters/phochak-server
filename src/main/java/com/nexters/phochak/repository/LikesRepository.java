package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Likes;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<Likes> findByUserAndPost(User user, Post post);
}
