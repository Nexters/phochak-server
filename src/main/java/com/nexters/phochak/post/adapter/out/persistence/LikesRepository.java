package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<LikesEntity, Long>, LikesCustomRepository {
    boolean existsByUserAndPost(UserEntity user, PostEntity postEntity);
    Optional<LikesEntity> findByUserAndPost(UserEntity user, PostEntity postEntity);
}
