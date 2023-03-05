-- 신고
create unique index idx01_unique_report_post on report_post (user_id, post_id);
