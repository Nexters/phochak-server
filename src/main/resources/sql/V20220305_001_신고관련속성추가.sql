-- 신고
create unique index idx01_unique_report_post on report_post (user_id, post_id);

-- 게시글
ALTER TABLE post ADD is_blind CHAR(1) DEFAULT 'N' NOT NULL;
