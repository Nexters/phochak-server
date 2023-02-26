-- 게시글
create index idx01_post on post (view, post_id);
create index idx02_post on post (user_id);

-- 좋아요
create index idx01_likes on likes (post_id);
create unique index idx02_unique_likes on likes (user_id, post_id);
