package com.nexters.phochak.domain;

import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.ShortsStateEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@ToString
@Getter
@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SHORTS_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shorts shorts;

    @Enumerated(EnumType.STRING)
    private ShortsStateEnum shortsStateEnum;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long view;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategoryEnum postCategory;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    public Post() {
    }

    @Builder
    public Post(User user, Shorts shorts, PostCategoryEnum postCategory) {
        this.shortsStateEnum = ShortsStateEnum.IN_PROGRESS;
        this.user = user;
        this.shorts = shorts;
        this.postCategory = postCategory;
        this.view = 0L;
    }

    public void setShorts(Shorts shorts) {
        this.shorts = shorts;
    }

    public void updateShortsState(ShortsStateEnum shortsStateEnum) {
        this.shortsStateEnum = shortsStateEnum;
    }
}
