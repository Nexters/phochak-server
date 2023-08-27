package com.nexters.phochak.post.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.hashtag.domain.Hashtag;
import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.report.domain.ReportPost;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.type.YesNoConverter;

import java.util.List;

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
    private UserEntity user;

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
    @Convert(converter = YesNoConverter.class)
    private boolean isBlind;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    @OneToMany(mappedBy = "post")
    private List<Hashtag> hashtags;

    public Post() {
    }

    @Builder
    public Post(UserEntity userEntity, Shorts shorts, PostCategoryEnum postCategory) {
        this.user = userEntity;
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
