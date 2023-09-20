package com.nexters.phochak.post.domain;

import com.nexters.phochak.post.adapter.out.persistence.ReportPostEntity;
import com.nexters.phochak.shorts.adapter.out.persistence.ShortsEntity;
import com.nexters.phochak.user.domain.User;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Post {
    private Long id;
    private PostCategoryEnum postCategory;
    private Long view = 0L;
    private boolean isBlind = false;
    private User user;
    private ShortsEntity shortsEntity;
    private List<ReportPostEntity> reportPostEntity = new ArrayList<>();
    private List<Likes> likes = new ArrayList<>();
    private List<Hashtag> hashtagList = new ArrayList<>();

    public Post(final Long id, final User user, final ShortsEntity shortsEntity, final List<ReportPostEntity> reportPostEntity, final Long view, final PostCategoryEnum postCategory, final boolean isBlind) {
        this.id = id;
        this.user = user;
        this.shortsEntity = shortsEntity;
        this.reportPostEntity = reportPostEntity;
        this.view = view;
        this.postCategory = postCategory;
        this.isBlind = isBlind;
    }

    public Post(final User user, final PostCategoryEnum postCategory) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(postCategory, "postCategory must not be null");
        this.user = user;
        this.postCategory = postCategory;
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void setShortsEntity(final ShortsEntity shortsEntity) {
        this.shortsEntity = shortsEntity;
    }

    public void updateContent(final PostCategoryEnum postCategoryEnum) {
        this.postCategory = postCategoryEnum;
    }

    public void setHashtagList(final List<Hashtag> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public void blind() {
        this.isBlind = true;
    }
}
