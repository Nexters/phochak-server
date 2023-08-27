package com.nexters.phochak.report.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

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
    private UserEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public ReportPost() {
    }

    @Builder
    public ReportPost(Long id, UserEntity reporter, Post post) {
        this.id = id;
        this.reporter = reporter;
        this.post = post;
    }
}
