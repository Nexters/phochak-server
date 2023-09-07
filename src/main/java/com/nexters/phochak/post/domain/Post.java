package com.nexters.phochak.post.domain;

import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.post.adapter.out.persistence.Hashtag;
import com.nexters.phochak.report.domain.ReportPost;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class Post {
    private Long id;
    private UserEntity user;
    private Shorts shorts;
    private List<ReportPost> reportPost;
    private Long view;
    private PostCategoryEnum postCategory;
    private boolean isBlind;
    private List<Likes> likes;
    private List<Hashtag> hashtags;

    public Post(final Long id, final UserEntity user, final Shorts shorts, final List<ReportPost> reportPost, final Long view, final PostCategoryEnum postCategory, final boolean isBlind, final List<Likes> likes, final List<Hashtag> hashtags) {
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
}
