package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;

@Getter
public class Likes {
    private final User user;
    private final Post post;

    public Likes(final User user, final Post post) {

        this.user = user;
        this.post = post;
    }
}
