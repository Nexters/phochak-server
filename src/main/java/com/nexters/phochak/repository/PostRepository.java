package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
