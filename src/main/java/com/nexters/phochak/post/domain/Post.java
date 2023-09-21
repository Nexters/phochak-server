package com.nexters.phochak.post.domain;

import com.nexters.phochak.post.adapter.out.persistence.ReportPostEntity;
import com.nexters.phochak.shorts.domain.Shorts;
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
    private Shorts shorts;
    private List<ReportPostEntity> reportPostEntity = new ArrayList<>();
    private List<Likes> likes = new ArrayList<>();
    private List<Hashtag> hashtagList = new ArrayList<>();

    public Post(final User user, final PostCategoryEnum postCategory) {
        validateConstructor(user, postCategory);
        this.user = user;
        this.postCategory = postCategory;
    }

    private static void validateConstructor(final User user, final PostCategoryEnum postCategory) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(postCategory, "postCategory must not be null");
    }

    private Post(final Long id, final User user, final Shorts shorts, final List<ReportPostEntity> reportPostEntity, final Long view, final PostCategoryEnum postCategory, final boolean isBlind) {
        this.id = id;
        this.user = user;
        this.shorts = shorts;
        this.reportPostEntity = reportPostEntity;
        this.view = view;
        this.postCategory = postCategory;
        this.isBlind = isBlind;
    }

    public static Post forMapper(
            final Long id,
            final User user,
            final Shorts shorts,
            final List<ReportPostEntity> reportPostEntity,
            final Long view,
            final PostCategoryEnum postCategory,
            final boolean isBlind) {
        return new Post(id, user, shorts, reportPostEntity, view, postCategory, isBlind);
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void setShorts(final Shorts shorts) {
        this.shorts = shorts;
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
