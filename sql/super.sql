/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50635
 Source Host           : localhost:3306
 Source Schema         : super

 Target Server Type    : MySQL
 Target Server Version : 50635
 File Encoding         : 65001

 Date: 08/06/2023 17:00:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `images` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片，最多9张，多张以\",\"隔开',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章',
  `liked_num` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '点赞数量',
  `comments_num` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '评论数量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, 1, '111', 'd4958247-4efd-4b75-988c-a29a6ca8cea8.jpg,b8e5722c-34e9-43c5-9910-ba1a14a46749.jpg', '222', 3, 5, '2023-06-03 16:55:19', '2023-06-08 16:22:57');
INSERT INTO `blog` VALUES (2, 1, '111', NULL, '2223', 2, 8, '2023-06-03 17:01:42', '2023-06-05 22:39:56');

-- ----------------------------
-- Table structure for blog_comments
-- ----------------------------
DROP TABLE IF EXISTS `blog_comments`;
CREATE TABLE `blog_comments`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `blog_id` bigint(20) UNSIGNED NOT NULL COMMENT '博文id',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '关联的1级评论id，如果是一级评论，则值为0',
  `answer_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '回复的评论id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复的内容',
  `liked_num` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '状态，0：正常，1：被举报，2：禁止查看',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of blog_comments
-- ----------------------------
INSERT INTO `blog_comments` VALUES (1, 1, 1, NULL, NULL, 'hello world', 1, 0, '2023-06-08 12:59:53', '2023-06-08 16:55:09');
INSERT INTO `blog_comments` VALUES (2, 1, 1, NULL, NULL, '111', 0, 0, '2023-06-08 13:18:33', '2023-06-08 13:18:33');
INSERT INTO `blog_comments` VALUES (3, 1, 1, NULL, NULL, '2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222', 0, 0, '2023-06-08 13:20:52', '2023-06-08 13:20:52');
INSERT INTO `blog_comments` VALUES (4, 1, 1, NULL, NULL, '222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222', 0, 0, '2023-06-08 13:22:44', '2023-06-08 13:22:44');
INSERT INTO `blog_comments` VALUES (5, 1, 1, NULL, NULL, '111', 0, 0, '2023-06-08 13:30:16', '2023-06-08 13:30:16');

-- ----------------------------
-- Table structure for blog_like
-- ----------------------------
DROP TABLE IF EXISTS `blog_like`;
CREATE TABLE `blog_like`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `blog_id` bigint(20) NOT NULL COMMENT '博文id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of blog_like
-- ----------------------------

-- ----------------------------
-- Table structure for comment_like
-- ----------------------------
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comment_id` bigint(20) NOT NULL COMMENT '评论id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comment_like
-- ----------------------------

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `follow_user_id` bigint(20) UNSIGNED NOT NULL COMMENT '关注的用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of follow
-- ----------------------------

-- ----------------------------
-- Table structure for sign
-- ----------------------------
DROP TABLE IF EXISTS `sign`;
CREATE TABLE `sign`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `year` year NOT NULL COMMENT '签到的年',
  `month` tinyint(2) NOT NULL COMMENT '签到的月',
  `date` date NOT NULL COMMENT '签到的日期',
  `is_backup` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否补签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sign
-- ----------------------------

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tag_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '上传用户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父标签id',
  `is_parent` tinyint(4) NOT NULL COMMENT '0-不是父标签，1-父标签',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniIdx_tagName`(`tag_name`) USING BTREE,
  INDEX `Idx_userId`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '队伍名称',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `max_num` int(11) NOT NULL DEFAULT 1 COMMENT '最大人数',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '0 - 公开，1 - 私有，2 - 加密',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '队伍' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES (1, '蓝桥杯1', '蓝桥杯竞赛', 1, '2023-06-30 22:00:38', 1, 0, '', '2023-05-17 12:36:45', '2023-06-04 22:00:42', 0);
INSERT INTO `team` VALUES (2, 'oneTop', 'oneTop', 4, '2023-06-30 22:00:38', 10, 0, NULL, '2023-05-17 17:29:35', '2023-06-04 22:00:45', 0);
INSERT INTO `team` VALUES (3, 'test', '加密队伍', 5, NULL, 2, 2, '1234', '2023-05-21 12:59:13', '2023-05-24 21:31:25', 1);
INSERT INTO `team` VALUES (4, 'test2', NULL, 5, NULL, 2, 2, '1234', '2023-05-21 13:26:26', '2023-05-24 21:30:19', 0);
INSERT INTO `team` VALUES (5, 'test', '加密队伍', 5, NULL, 2, 2, '1234', '2023-05-21 12:59:13', '2023-05-24 21:30:20', 0);
INSERT INTO `team` VALUES (6, 'test', '加密队伍', 5, NULL, 2, 2, '1234', '2023-05-21 12:59:13', '2023-05-24 21:30:21', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `user_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `avatar_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别 0-女 1-男 2-保密',
  `profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int(11) NULL DEFAULT 0 COMMENT '用户状态，0为正常',
  `role` int(11) NOT NULL DEFAULT 0 COMMENT '用户角色 0-普通用户,1-管理员',
  `tags` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签列表',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniIdx_account`(`user_account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'ochiamalu', '2a1e9392cd2ca500456941ec1345e626', 'ochiamalu', 'http://niu.ochiamalu.xyz/zhiyue.jpg', 1, '', '13738728682', 'linzhehao1108@126.com', 0, 1, '[\"java\",\"大一\",\"男\"]', '2023-05-17 12:33:29', '2023-06-04 19:40:46', 0);
INSERT INTO `user` VALUES (2, 'tH8SazReq1', '11111111', 'th8sazreq1', 'http://niu.ochiamalu.xyz/75e31415779979ae40c4c0238aa4c34.jpg', 0, '我是一个有耐心的作家。', '17579910380', 'tH8SazReq1@hotmail.com', 0, 0, '[\"html/css\", \"c\", \"大二\", \"保密\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (3, 'vOyXQ865Xi', '11111111', 'voyxq865xi', 'http://niu.ochiamalu.xyz/12d4949b4009d089eaf071aef0f1f40.jpg', 0, '我是一个无聊的作家。', '18685439434', 'vOyXQ865Xi@hotmail.com', 0, 0, '[\"c#\", \"html/css\", \"高二\", \"保密\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (4, 't1UXa2FzyT', '11111111', 't1uxa2fzyt', 'http://niu.ochiamalu.xyz/22fe8428428c93a565e181782e97654.jpg', 1, '我是一个有毅力的艺术家。', '18940806775', 't1UXa2FzyT@yahoo.com', 0, 0, '[\"c++\", \"c#\", \"高三\", \"男\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (5, 'HUNyIuAffu', '11111111', 'hunyiuaffu', 'http://niu.ochiamalu.xyz/905731909dfdafd0b53b3c4117438d3.jpg', 0, '我是一个有野心的工程师。', '17421786943', 'HUNyIuAffu@yandex.com', 0, 0, '[\"c++\", \"react\", \"高一\", \"保密\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (6, 'EiOXAqto3S', '11111111', 'eioxaqto3s', 'http://niu.ochiamalu.xyz/f870176b1a628623fa7fe9918b862d7.jpg', 1, '我是一个有野心的政治家。', '17439796595', 'EiOXAqto3S@protonmail.com', 0, 0, '[\"vue\", \"大三\", \"女\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (7, 'kKugEJWQwf', '11111111', 'kkugejwqwf', 'http://niu.ochiamalu.xyz/f870176b1a628623fa7fe9918b862d7.jpg', 1, '我是一个有创意的画家。', '11914380574', 'kKugEJWQwf@yandex.com', 0, 0, '[\"c\", \"python\", \"研究生\", \"女\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (8, 'HIQ6FaTpFi', '11111111', 'hiq6fatpfi', 'http://niu.ochiamalu.xyz/905731909dfdafd0b53b3c4117438d3.jpg', 1, '我是一个有智慧的作家。', '17000407484', 'HIQ6FaTpFi@protonmail.com', 0, 0, '[\"vue\", \"已工作\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (9, 'DzPfP97Uko', '11111111', 'dzpfp97uko', 'http://niu.ochiamalu.xyz/75e31415779979ae40c4c0238aa4c34.jpg', 0, '我是一个搞笑的经理。', '11895377518', 'DzPfP97Uko@inbox.com', 0, 0, '[\"c\", \"html/css\", \"研究生\", \"保密\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (10, '2LW5vFyYDD', '11111111', '2lw5vfyydd', 'http://niu.ochiamalu.xyz/22fe8428428c93a565e181782e97654.jpg', 1, '我是一个有趣的程序员。', '11793323314', '2LW5vFyYDD@mail.com', 0, 0, '[\"c#\", \"大四\", \"女\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);
INSERT INTO `user` VALUES (11, 'MomxLE7eq8', '11111111', 'momxle7eq8', 'http://niu.ochiamalu.xyz/cccfb0995f5d103414bd8a8bd742c34.jpg', 0, '我是一个搞笑的演员。', '10756286548', 'MomxLE7eq8@aol.com', 0, 0, '[\"html/css\", \"大一\", \"保密\"]', '2023-05-28 19:04:56', '2023-05-28 19:04:56', 0);

-- ----------------------------
-- Table structure for user_team
-- ----------------------------
DROP TABLE IF EXISTS `user_team`;
CREATE TABLE `user_team`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `team_id` bigint(20) NULL DEFAULT NULL COMMENT '队伍id',
  `join_time` datetime NULL DEFAULT NULL COMMENT '加入时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户队伍关系' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_team
-- ----------------------------
INSERT INTO `user_team` VALUES (1, 1, 2, '2023-05-28 19:03:55', '2023-05-28 19:03:54', '2023-05-28 19:03:54', 0);
INSERT INTO `user_team` VALUES (2, 1, 1, '2023-05-28 19:05:24', '2023-05-28 19:05:24', '2023-05-28 19:05:24', 0);

SET FOREIGN_KEY_CHECKS = 1;
