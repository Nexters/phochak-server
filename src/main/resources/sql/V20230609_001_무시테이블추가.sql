-- 유저가 다른 유저를 무시하는 테이블
create table ignored_users (
   user_id bigint not null,
   ignored_user_id bigint,
   primary key (user_id)
) engine=InnoDB;

-- multiple unique 인덱스 추가
alter table ignored_users add constraint idx02_unique_ignored_user unique (user_id, ignored_user_id);