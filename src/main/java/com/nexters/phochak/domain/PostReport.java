package com.nexters.phochak.domain;

import com.nexters.phochak.specification.ReportCategory;
import lombok.Builder;

import javax.persistence.*;

@Entity
public class PostReport {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="POST_REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="REPORTER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User reporter;

    @Enumerated(EnumType.STRING)
    private ReportCategory reportCategory;

    private String title;

    private String content;

    public PostReport() {
    }

    @Builder
    public PostReport(Post post, User reporter, ReportCategory reportCategory, String title, String content) {
        this.post = post;
        this.reporter = reporter;
        this.reportCategory = reportCategory;
        this.title = title;
        this.content = content;
    }
}
