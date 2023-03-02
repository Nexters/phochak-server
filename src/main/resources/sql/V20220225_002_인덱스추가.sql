-- 유저
create index idx01_user on user (nickname);

-- 게시글
create index idx01_post on post (view, post_id);
create index idx02_post on post (user_id);

-- 쇼츠
create index idx01_shorts on shorts (upload_key);

-- 좋아요
create index idx01_likes on likes (post_id);
create index idx02_unique_likes on likes (user_id, post_id)