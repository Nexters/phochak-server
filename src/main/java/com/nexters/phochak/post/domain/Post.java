package com.nexters.phochak.post.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.hashtag.domain.Hashtag;
import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.report.domain.ReportPost;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@ToString
@Getter
@Entity
@Table(indexes =
        {@Index(name = "idx01_post", columnList = "view, post_id"),
        @Index(name = "idx02_post", columnList = "user_id")})
public class Post extends BaseTime {
    private static final Long BLOCK_CRITERIA = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "SHORTS_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shorts shorts;

    @OneToMany(mappedBy = "post")
    private List<ReportPost> reportPost;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long view;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategoryEnum postCategory;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Type(type = "yes_no")
    private boolean isBlind;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    @OneToMany(mappedBy = "post")
    private List<Hashtag> hashtags;

    public Post() {
    }

    @Builder
    public Post(User user, Shorts shorts, PostCategoryEnum postCategory) {
        this.user = user;
        this.shorts = shorts;
        this.postCategory = postCategory;
        this.isBlind = false;
        this.view = 0L;
    }

    public void setShorts(Shorts shorts) {
        this.shorts = shorts;
    }

    public void blindPostIfRequired(Long reportCount) {
        if (reportCount >= BLOCK_CRITERIA) {
            this.isBlind = true;
        }
    }

    public void updateContent(PostCategoryEnum postCategory) {
        this.postCategory = postCategory;
    }
}
