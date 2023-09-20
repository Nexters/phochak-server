package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;

public class ReportPost {
    private static final Long BLOCK_CRITERIA = 5L;
    private final User user;
    private final Post post;

    public ReportPost(final User user, final Post post) {
        this.user = user;
        this.post = post;
    }

    public boolean checkPenaltyToBlind(final int reportCount) {
        return reportCount >= BLOCK_CRITERIA;
    }
}
