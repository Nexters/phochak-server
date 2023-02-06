package com.nexters.phochak.domain;

import com.nexters.phochak.specification.ReportCategoryEnum;
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
    private ReportCategoryEnum reportCategoryEnum;

    private String title;

    private String content;

    public PostReport() {
    }

    @Builder
    public PostReport(Post post, User reporter, ReportCategoryEnum reportCategoryEnum, String title, String content) {
        this.post = post;
        this.reporter = reporter;
        this.reportCategoryEnum = reportCategoryEnum;
        this.title = title;
        this.content = content;
    }
}
