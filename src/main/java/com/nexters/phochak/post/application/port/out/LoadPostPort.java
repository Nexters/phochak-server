package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;

import java.util.List;

public interface LoadPostPort {
    Post load(Long postId);

    List<Post> loadAllPostByUser(User user);
}
