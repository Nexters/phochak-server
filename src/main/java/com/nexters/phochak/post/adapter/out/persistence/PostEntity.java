package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.adapter.out.persistence.ShortsEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
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
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.type.YesNoConverter;

import java.util.List;

@Getter
@Entity
@Table(indexes =
        {@Index(name = "idx01_post", columnList = "view, post_id"),
        @Index(name = "idx02_post", columnList = "user_id")})
public class PostEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "SHORTS_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ShortsEntity shorts;

    @OneToMany(mappedBy = "post")
    private List<ReportPostEntity> reportPostEntity;

    @Column(name = "VIEW", nullable = false)
    @ColumnDefault("0")
    private Long view;

    @Column(name = "POST_CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategoryEnum postCategory;

    @Column(name = "IS_BLIND", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Convert(converter = YesNoConverter.class)
    private boolean isBlind;

    @OneToMany(mappedBy = "post")
    private List<LikesEntity> likes;

    @OneToMany(mappedBy = "post")
    private List<HashtagEntity> hashtags;

    public PostEntity() {
    }

    public PostEntity(final Long id, final UserEntity user, final ShortsEntity shorts, final List<ReportPostEntity> reportPostEntity, final Long view, final PostCategoryEnum postCategory, final boolean isBlind) {
        this.id = id;
        this.user = user;
        this.shorts = shorts;
        this.reportPostEntity = reportPostEntity;
        this.view = view;
        this.postCategory = postCategory;
        this.isBlind = isBlind;
    }

    public void setShorts(ShortsEntity shorts) {
        this.shorts = shorts;
    }

}
