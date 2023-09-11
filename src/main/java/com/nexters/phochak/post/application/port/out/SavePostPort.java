package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

public interface SavePostPort {
    void save(Post post);
}
