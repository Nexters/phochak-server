package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    Optional<Post> findByShorts(Shorts shorts);
}
