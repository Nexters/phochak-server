package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final UserMapper userMapper;
    public PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getId(),
                userMapper.toEntity(post.getUser()),
                post.getShorts(),
                post.getReportPost(),
                post.getView(),
                post.getPostCategory(),
                post.isBlind(),
                post.getLikes(),
                post.getHashtagEntities()
        );
    }

    public Post toDomain(PostEntity postEntity) {
        return new Post(
                postEntity.getId(),
                userMapper.toDomain(postEntity.getUser()),
                postEntity.getShorts(),
                postEntity.getReportPost(),
                postEntity.getView(),
                postEntity.getPostCategory(),
                postEntity.isBlind(),
                postEntity.getLikes(),
                postEntity
                        .getHashtags());
    }
}
