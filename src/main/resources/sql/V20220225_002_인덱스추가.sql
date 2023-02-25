-- 유저
create index idx01_user on user (nickname);

-- 게시글
create index idx01_post on post (view, post_id);
create index idx02_post on post (user_id);

-- 해시태그
create index idx01_hashtag on hashtag (tag);

-- 좋아요
create index idx01_likes on likes (post_id);
