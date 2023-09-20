package com.nexters.phochak.post.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;

@Getter
public class ReportPost {
    private Long id;
    private final User user;
    private final Post post;
    private static final Long BLOCK_CRITERIA = 5L;

    public ReportPost(final User user, final Post post) {
        this.user = user;
        this.post = post;
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public boolean checkPenaltyToBlind(final int reportCount) {
        return reportCount >= BLOCK_CRITERIA;
    }
}
