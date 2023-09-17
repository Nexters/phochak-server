package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Likes;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;

public interface LoadLikesPort {
    Likes load(User user, Post post);
}
