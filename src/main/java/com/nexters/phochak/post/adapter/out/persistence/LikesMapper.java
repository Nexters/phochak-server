package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Likes;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikesMapper {
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    public LikesEntity toEntity(Likes like) {
        return new LikesEntity(
                userMapper.toEntity(like.getUser()),
                postMapper.toEntity(like.getPost())
        );
    }

    public Likes toDomain(LikesEntity likeEntity) {
        return new Likes(
                likeEntity.getId(),
                userMapper.toDomain(likeEntity.getUser()),
                postMapper.toDomain(likeEntity.getPost()));
    }

}
