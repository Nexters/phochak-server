create table `user`
(
    user_id         bigint       not null auto_increment,
    created_at      datetime(6),
    updated_at      datetime(6),
    nickname        varchar(10)  not null unique,
    profile_img_url varchar(255),
    provider        varchar(255),
    provider_id     varchar(255) not null unique,
    primary key (user_id)
) engine=InnoDB

create table hashtag
(
    tag_id  bigint not null auto_increment,
    tag     varchar(20),
    post_id bigint,
    primary key (tag_id)
) engine=InnoDB

create table likes
(
    likes_id   bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    post_id    bigint,
    user_id    bigint,
    primary key (likes_id)
) engine=InnoDB

create table post
(
    post_id       bigint           not null auto_increment,
    created_at    datetime(6),
    updated_at    datetime(6),
    post_category varchar(255)     not null,
    view          bigint default 0 not null,
    shorts_id     bigint,
    user_id       bigint,
    primary key (post_id)
) engine=InnoDB

create table refresh_token
(
    refresh_token_id     bigint not null auto_increment,
    refresh_token_string varchar(255),
    user_id              bigint,
    primary key (refresh_token_id)
) engine=InnoDB

create table report_post
(
    report_id  bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    reason     varchar(255),
    post_id    bigint not null,
    user_id    bigint not null,
    primary key (report_id)
) engine=InnoDB

create table shorts
(
    shorts_id         bigint       not null auto_increment,
    shorts_state_enum varchar(255) not null,
    shorts_url        varchar(255) not null unique,
    thumbnail_url     varchar(255) not null unique,
    upload_key        varchar(255) not null unique,
    primary key (shorts_id)
) engine=InnoDB
