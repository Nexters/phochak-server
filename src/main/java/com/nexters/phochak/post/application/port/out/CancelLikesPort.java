package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Likes;

public interface CancelLikesPort {
    void cancel(Likes likes);
}
