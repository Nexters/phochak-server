package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;

@Getter
public class Likes {
    private Long id;
    private final User user;
    private final Post post;

    public Likes(final User user, final Post post) {
        validateConstructor(user, post);
        this.user = user;
        this.post = post;
    }

    private static void validateConstructor(final User user, final Post post) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(post, "post must not be null");
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
