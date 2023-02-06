use dingtalk;

#-------------------- 用户模块新增的表 -----------------------#
drop table if exists team;
create table `team`
(
    id   int primary key AUTO_INCREMENT comment '组id',
    name char(20) not null comment '组名'
) comment '用户组表';

drop table if exists permission;
create table `permission`
(
    id          int primary key AUTO_INCREMENT comment '权限id',
    name        char(20)  not null comment '权限名',
    description char(100) not null comment '权限描述'
) comment '权限表';

drop table if exists user_permission;
create table `user_permission`
(
    user_id int not null comment '用户id',
    permission_id int not null comment '权限id',
    primary key (user_id, permission_id),
    foreign key (user_id) references user (id),
    foreign key (permission_id) references permission (id)
);

drop table if exists user_team;
create table `user_team`
(
    user_id int not null comment '用户id',
    team_id int not null comment '组id',
    primary key (user_id, team_id),
    foreign key (user_id) references user (id),
    foreign key (team_id) references team (id)
) comment '用户与组关系表';
