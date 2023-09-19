package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

import java.util.List;

public interface RemoveShortsObjectPort {
    void remove(List<Post> postList);
}
