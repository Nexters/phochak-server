package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

import java.util.List;

public interface DeleteShortsPort {
    void deleteAllIn(List<Post> postList);
}
