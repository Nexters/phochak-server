package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class ReportPost {
    private Long id;
    private final User user;
    private final Post post;
    private static final Long BLOCK_CRITERIA = 5L;

    public ReportPost(final User user, final Post post) {
        validateConstructor(user, post);
        this.user = user;
        this.post = post;
    }

    private static void validateConstructor(final User user, final Post post) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(post, "post must not be null");
    }

    private ReportPost(final Long id, final User user, final Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public static ReportPost forMapper(final Long id, final User user, final Post post) {
        return new ReportPost(id, user, post);
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public boolean checkPenaltyToBlind(final int reportCount) {
        return reportCount >= BLOCK_CRITERIA;
    }
}
