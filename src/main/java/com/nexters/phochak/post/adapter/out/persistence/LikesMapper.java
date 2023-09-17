package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Likes;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikesMapper {

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public LikesEntity toEntity(final Likes likes) {
        return new LikesEntity(
                userMapper.toEntity(likes.getUser()),
                postMapper.toEntity(likes.getPost()));
    }

    public Likes toDomain(final LikesEntity likesEntity) {
        return new Likes(
                userMapper.toDomain(likesEntity.getUser()),
                postMapper.toDomain(likesEntity.getPost()));
    }
}
