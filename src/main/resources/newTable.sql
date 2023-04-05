use dingtalk;

#-------------------- 用户模块新增的表 -----------------------#
create table if not exists `team`
(
    id   int primary key AUTO_INCREMENT comment '组id',
    name char(20) not null comment '组名'
) comment '用户组表';

create table if not exists `permission`
(
    id          int primary key AUTO_INCREMENT comment '权限id',
    name        char(20)  not null comment '权限名',
    description char(100) not null comment '权限描述'
) comment '权限表';

create table if not exists `user_permission`
(
    user_id int not null comment '用户id',
    permission_id int not null comment '权限id',
    primary key (user_id, permission_id),
    foreign key (user_id) references user (id),
    foreign key (permission_id) references permission (id)
);

create table if not exists `user_team`
(
    user_id int not null comment '用户id',
    team_id int not null comment '组id',
    primary key (user_id, team_id),
    foreign key (user_id) references user (id),
    foreign key (team_id) references team (id)
) comment '用户与组关系表';

create table if not exists `news`
(
    id              int primary key AUTO_INCREMENT comment '公告id',
    title           varchar(255) not null comment '公告标题',
    link            varchar(255) not null comment '公告链接',
    author_id       int not null comment '发布公告作者id',
    release_time    datetime not null default CURRENT_TIMESTAMP,
    is_deleted      int not null comment '是否被删除',
    is_shown        int not null comment '是否被删除',
    foreign key (author_id) references user (id)
) comment '公告表';