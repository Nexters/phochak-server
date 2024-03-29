package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

import java.util.List;

public interface DeleteHashtagPort {
    void deleteAllByPost(Post post);

    void deleteAllByPostIdIn(List<Post> postList);
}
