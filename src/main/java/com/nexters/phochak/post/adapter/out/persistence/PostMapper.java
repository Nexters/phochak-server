package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.adapter.out.persistence.ShortsMapper;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final UserMapper userMapper;
    private final ShortsMapper shortsMapper;

    public PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getId(),
                userMapper.toEntity(post.getUser()),
                shortsMapper.toEntity(post.getShorts()),
                post.getReportPostEntity(),
                post.getView(),
                post.getPostCategory(),
                post.isBlind());
    }

    public Post toDomain(PostEntity postEntity) {
        return Post.forMapper(
                postEntity.getId(),
                userMapper.toDomain(postEntity.getUser()),
                shortsMapper.toDomain(postEntity.getShorts()),
                postEntity.getReportPostEntity(),
                postEntity.getView(),
                postEntity.getPostCategory(),
                postEntity.isBlind());
    }
}
