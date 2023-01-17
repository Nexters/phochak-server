package com.nexters.phochak.domain;

import com.nexters.phochak.specification.PostCategory;
import lombok.Builder;

import javax.persistence.*;

@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne
    @JoinColumn(name="SHORTS_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shorts shorts;

    private Long view;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    public Post() {
    }

    @Builder
    public Post(User user, Shorts shorts, Long view, PostCategory postCategory) {
        this.user = user;
        this.shorts = shorts;
        this.view = view;
        this.postCategory = postCategory;
    }
}
