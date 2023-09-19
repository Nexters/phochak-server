package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;

@Getter
public class Likes {
    private Long id;
    private final User user;
    private final Post post;

    public Likes(final User user, final Post post) {
        this.id = null;
        this.user = user;
        this.post = post;
    }

    public Likes(final Long id, final Post post, final User user) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
