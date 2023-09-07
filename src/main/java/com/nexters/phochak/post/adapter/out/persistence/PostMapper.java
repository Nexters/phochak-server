package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostEntity toEntity(Post post) {
        return PostEntity.toDomain(post);
    }

    public Post toDomain(PostEntity postEntity) {
        return new Post(
                postEntity.getId(),
                postEntity.getUser(),
                postEntity.getShorts(),
                postEntity.getReportPost(),
                postEntity.getView(),
                postEntity.getPostCategory(),
                postEntity.isBlind(),
                postEntity.getLikes(),
                postEntity.getHashtags());
    }
}
