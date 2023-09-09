package com.nexters.phochak.post.domain;

import com.nexters.phochak.post.adapter.out.persistence.Hashtag;
import com.nexters.phochak.post.adapter.out.persistence.Likes;
import com.nexters.phochak.post.adapter.out.persistence.ReportPost;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.user.domain.User;
import lombok.Getter;

import java.util.List;

@Getter
public class Post {
    private Long id;
    private User user;
    private Shorts shorts;
    private List<ReportPost> reportPost;
    private Long view;
    private PostCategoryEnum postCategory;
    private boolean isBlind;
    private List<Likes> likes;
    private List<Hashtag> hashtags;

    public Post(final Long id, final User user, final Shorts shorts, final List<ReportPost> reportPost, final Long view, final PostCategoryEnum postCategory, final boolean isBlind, final List<Likes> likes, final List<Hashtag> hashtags) {
        this.id = id;
        this.user = user;
        this.shorts = shorts;
        this.reportPost = reportPost;
        this.view = view;
        this.postCategory = postCategory;
        this.isBlind = isBlind;
        this.likes = likes;
        this.hashtags = hashtags;
    }

    public Post(final User user, final PostCategoryEnum postCategory) {
        this.user = user;
        this.postCategory = postCategory;
    }
}
