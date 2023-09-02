package com.nexters.phochak.likes.domain;

import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long>, LikesCustomRepository {
    boolean existsByUserAndPost(UserEntity user, PostEntity postEntity);
    Optional<Likes> findByUserAndPost(UserEntity user, PostEntity postEntity);
}
