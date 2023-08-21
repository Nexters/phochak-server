package com.nexters.phochak.report.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Entity
@Table(indexes = {@Index(name = "idx01_unique_report_post", columnList = "USER_ID, POST_ID", unique = true)})
public class ReportPost extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public ReportPost() {
    }

    @Builder
    public ReportPost(Long id, User reporter, Post post) {
        this.id = id;
        this.reporter = reporter;
        this.post = post;
    }
}
