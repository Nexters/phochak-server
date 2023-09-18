package com.nexters.phochak.post.domain;

import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import org.springframework.util.Assert;

public class Hashtag {
    public static final int HASHTAG_MAX_SIZE = 20;
    private Long id;
    private PostEntity post;
    private String tag;

    public Hashtag(PostEntity post, String tag) {
        validateConstructor(post, tag);
        this.post = post;
        this.tag = tag;
    }

    private static void validateConstructor(final PostEntity post, final String tag) {
        Assert.notNull(post, "post must not be null");
        Assert.notNull(tag, "tag must not be null");
        if (tag.length() > HASHTAG_MAX_SIZE) {
            throw new IllegalArgumentException("tag must not be longer than " + HASHTAG_MAX_SIZE);
        }
    }
}
