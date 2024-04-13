create table if not exists blog
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_id      bigint                                    not null comment '用户id',
    title        varchar(255) collate utf8mb4_unicode_ci   not null comment '标题',
    images       varchar(2048)                             null comment '图片，最多9张，多张以","隔开',
    content      varchar(2048) collate utf8mb4_unicode_ci  not null comment '文章',
    liked_num    int(8) unsigned default 0                 null comment '点赞数量',
    comments_num int(8) unsigned default 0                 null comment '评论数量',
    create_time  timestamp       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  timestamp       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint         default 0                 null comment '逻辑删除'
)
    row_format = COMPACT;

create table if not exists blog_comments
(
    id          bigint auto_increment comment '主键'
        primary key,
    user_id     bigint                                    not null comment '用户id',
    blog_id     bigint                                    not null comment '博文id',
    parent_id   bigint unsigned                           null comment '关联的1级评论id，如果是一级评论，则值为0',
    answer_id   bigint unsigned                           null comment '回复的评论id',
    content     varchar(255)                              not null comment '回复的内容',
    liked_num   int(8) unsigned default 0                 null comment '点赞数',
    status      tinyint(1) unsigned                       null comment '状态，0：正常，1：被举报，2：禁止查看',
    create_time timestamp       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint         default 0                 null comment '逻辑删除'
)
    row_format = COMPACT;

create table if not exists blog_like
(
    id          bigint auto_increment comment '主键'
        primary key,
    blog_id     bigint                             not null comment '博文id',
    user_id     bigint                             not null comment '用户id',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 null comment '逻辑删除'
)
    charset = utf8
    row_format = COMPACT;

create table if not exists chat
(
    id          bigint auto_increment comment '聊天记录id'
        primary key,
    from_id     bigint                                  not null comment '发送消息id',
    to_id       bigint                                  null comment '接收消息id',
    text        varchar(512) collate utf8mb4_unicode_ci null,
    chat_type   tinyint                                 not null comment '聊天类型 1-私聊 2-群聊',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null,
    team_id     bigint                                  null,
    is_delete   tinyint  default 0                 null
)
    comment '聊天消息表' row_format = COMPACT;

create table if not exists comment_like
(
    id          bigint auto_increment comment '主键'
        primary key,
    comment_id  bigint                             not null comment '评论id',
    user_id     bigint                             not null comment '用户id',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 null comment '逻辑删除'
)
    row_format = COMPACT;

create table if not exists config
(
    id          bigint auto_increment comment '主键'
        primary key,
    value       varchar(255)                        null comment '数据',
    type        tinyint                             null comment '0-通知栏 2-轮播图',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint   default 0                 null comment '逻辑删除'
)
    charset = utf8
    row_format = COMPACT;

create table if not exists follow
(
    id             bigint auto_increment comment '主键'
        primary key,
    user_id        bigint                              not null comment '用户id',
    follow_user_id bigint                              not null comment '关注的用户id',
    create_time    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint   default 0                 null comment '逻辑删除'
)
    charset = utf8
    row_format = COMPACT;

create table if not exists friends
(
    id          bigint auto_increment comment '好友申请id'
        primary key,
    from_id     bigint                             not null comment '发送申请的用户id',
    receive_id  bigint                             null comment '接收申请的用户id ',
    is_read     tinyint  default 0                 not null comment '是否已读(0-未读 1-已读)',
    status      tinyint  default 0                 not null comment '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null,
    is_delete   tinyint  default 0                 not null comment '是否删除',
    remark      varchar(214)                       null comment '好友申请备注信息'
)
    comment '好友申请管理表' row_format = COMPACT;

create table if not exists message
(
    id          bigint auto_increment comment '主键'
        primary key,
    type        tinyint                            null comment '类型-1 点赞',
    from_id     bigint                             null comment '消息发送的用户id',
    to_id       bigint                             null comment '消息接收的用户id',
    data        varchar(255)                       null comment '消息内容',
    is_read     tinyint  default 0                 null comment '已读-0 未读 ,1 已读',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 null comment '逻辑删除'
)
    row_format = COMPACT;

create table if not exists sign
(
    id        bigint auto_increment comment '主键'
        primary key,
    user_id   bigint              not null comment '用户id',
    year      year                not null comment '签到的年',
    month     tinyint(2)          not null comment '签到的月',
    date      date                not null comment '签到的日期',
    is_backup tinyint(1) unsigned null comment '是否补签'
)
    charset = utf8
    row_format = COMPACT;

create table if not exists team
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(256)                       not null comment '队伍名称',
    description varchar(1024)                      null comment '描述',
    cover_image varchar(255)                       null comment '封面图片',
    max_num     int      default 1                 not null comment '最大人数',
    expire_time datetime                           null comment '过期时间',
    user_id     bigint                             null comment '用户id',
    status      int      default 0                 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password    varchar(512)                       null comment '密码',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete   tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍' row_format = COMPACT;

create table if not exists user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(255)                       null comment '用户昵称',
    password     varchar(512)                       not null comment '用户密码',
    user_account varchar(255)                       null comment '账号',
    avatar_url   varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别 0-女 1-男 2-保密',
    profile      varchar(255)                       null,
    phone        varchar(128)                       null comment '手机号',
    email        varchar(512)                       null comment '邮箱',
    status       int      default 0                 null comment '用户状态，0为正常',
    role         int      default 0                 not null comment '用户角色 0-普通用户,1-管理员',
    friend_ids   varchar(255)                       null,
    tags         varchar(1024)                      null comment '标签列表',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除'
)
    charset = utf8
    row_format = DYNAMIC;

create table if not exists user_team
(
    id          bigint auto_increment comment 'id'
        primary key,
    user_id     bigint                             null comment '用户id',
    team_id     bigint                             null comment '队伍id',
    join_time   datetime                           null comment '加入时间',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete   tinyint  default 0                 not null comment '是否删除'
)
    comment '用户队伍关系' charset = utf8
                           row_format = COMPACT;

