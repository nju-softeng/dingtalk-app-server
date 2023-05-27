use dingtalk;

#-------------------- 用户模块新增的表 -----------------------#
create table if not exists `team`
(
    id   int primary key AUTO_INCREMENT comment '组id',
    name varchar(255) not null comment '组名'
) comment '用户组表';
INSERT INTO dingtalk.team (id, name) VALUES (1, 'ESE');
INSERT INTO dingtalk.team (id, name) VALUES (2, 'PRO');
INSERT INTO dingtalk.team (id, name) VALUES (3, 'BDA');
INSERT INTO dingtalk.team (id, name) VALUES (4, 'ARC');
INSERT INTO dingtalk.team (id, name) VALUES (5, 'SEC');
INSERT INTO dingtalk.team (id, name) VALUES (6, 'BLC');
INSERT INTO dingtalk.team (id, name) VALUES (7, 'PHD');
INSERT INTO dingtalk.team (id, name) VALUES (8, 'Alumni');

create table if not exists `permission`
(
    id          int primary key AUTO_INCREMENT comment '权限id',
    name        varchar(255)  not null comment '权限名',
    description varchar(255) not null comment '权限描述'
) comment '权限表';
INSERT INTO dingtalk.permission (id, name, description) VALUES (1, '分配管理权限', '有权利分配管理员权限');
INSERT INTO dingtalk.permission (id, name, description) VALUES (2, '设置实习期', '有权利设置实习允许时间段');
INSERT INTO dingtalk.permission (id, name, description) VALUES (3, '编辑用户信息', '有权利修改任意用户信息（在读学位/学号/用户权限）');
INSERT INTO dingtalk.permission (id, name, description) VALUES (4, '编辑滚动公告栏', '有权利编辑滚动公告栏');
INSERT INTO dingtalk.permission (id, name, description) VALUES (5, '实习申请审核', '有权利通过/退回实习申请');
INSERT INTO dingtalk.permission (id, name, description) VALUES (6, '专利申请审核', '有权利通过/退回专利申请');
INSERT INTO dingtalk.permission (id, name, description) VALUES (7, '报销申请审核', '有权利通过/退回报销申请');
INSERT INTO dingtalk.permission (id, name, description) VALUES (8, '虚拟机申请审核', '有权利通过/退回虚拟机申请');
INSERT INTO dingtalk.permission (id, name, description) VALUES (9, '绩效申请审核', '有权利通过/退回绩效申请审核');
INSERT INTO dingtalk.permission (id, name, description) VALUES (10, '论文申请审核', '有权利设置论文的录用结果');
INSERT INTO dingtalk.permission (id, name, description) VALUES (11, '编辑标准参数', '有权利编辑标准参数');

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
    release_time    datetime not null default CURRENT_TIMESTAMP comment '发布时间',
    is_deleted      int not null comment '是否被删除',
    is_shown        int not null comment '是否显示',
    content         text comment '公告详情',
    foreign key (author_id) references user (id)
) comment '公告表';

create table if not exists  `internship_period_recommended`
(
    id              int primary key AUTO_INCREMENT comment '推荐实习时间段id',
    start           date comment '起始日期',
    end             date comment '终止日期',
    release_time    datetime not null default CURRENT_TIMESTAMP comment '发布时间',
    author_id       int not null comment '发布者id',
    foreign key (author_id) references user (id)
) comment '推荐实习周期表';

create table if not exists `patent_ac_record`
(
    id                  int primary key AUTO_INCREMENT comment '公告id',
    patent_id           int not null ,
    ac_record_id        int not null ,
#     内审通过为1，授权通过为2
    type                int not null,
    foreign key (patent_id) references patent (id),
    foreign key (ac_record_id) references ac_record (id)
);