package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

public interface DeletePostPort {

    void delete(Post post);
}
