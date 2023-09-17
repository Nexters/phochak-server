package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Likes;

public interface SaveLikesPort {
    void save(Likes likes);
}
