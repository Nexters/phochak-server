-- 해시태그
create index idx01_hashtag on post (tag);
create index idx02_hashtag on post (post_id);
