/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 127.0.0.1:3306
 Source Schema         : spring-security-demo

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 27/04/2023 09:40:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_initialization
CREATE TABLE `sys_initialization` (
  `id` varchar (32) NOT NULL,
  `super_user_id` varchar (32) NOT NULL,
  `initialized` tinyint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
CREATE TABLE `sys_permission`  (
  `id` varchar(32) NOT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `type` varchar(1) NOT NULL COMMENT 'D:目录 M:菜单 P:权限',
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `required` tinyint DEFAULT NULL,
  `menu_hidden` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_type`(`type`) USING BTREE,
  UNIQUE INDEX `unique_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
CREATE TABLE `sys_role`  (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `role_key` varchar(255) NOT NULL,
  `level` int NOT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_role_key`(`role_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
CREATE TABLE `sys_user`  (
  `id` varchar(32) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_intensity` varchar(1) DEFAULT NULL COMMENT 'L:低 M:中 H:高',
  `role_id` varchar(32) DEFAULT NULL,
  `sex` varchar(1) DEFAULT NULL COMMENT 'M:男 F:女 U:未知',
  `phone_number` varchar(11) default NULL,
  `create_time` datetime(3) DEFAULT NULL,
  `last_login_time` datetime(3) DEFAULT NULL,
  `last_update_password_time` datetime(3) DEFAULT NULL,
  `account_non_expired` tinyint DEFAULT NULL,
  `credentials_non_expired` tinyint DEFAULT NULL,
  `account_non_locked` tinyint DEFAULT NULL,
  `enabled` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_username`(`username`) USING BTREE,
  INDEX `index_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) NOT NULL,
  `r_id` varchar(32) NOT NULL,
  `p_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_r_p`(`r_id`, `p_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
CREATE TABLE `sys_log`  (
  `id` varchar(32) NOT NULL,
  `opt_user` varchar(255) DEFAULT NULL,
  `opt_desc` varchar(255) DEFAULT NULL,
  `result_code` int DEFAULT NULL,
  `err_code` varchar(32) DEFAULT NULL,
  `err_msg` varchar(8092) DEFAULT NULL,
  `opt_time` datetime(3) DEFAULT NULL,
  `cost_time` int DEFAULT NULL,
  `mac` varchar(64) DEFAULT NULL,
  `audited` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_opt_user_result_code_audited`(`opt_user`, `result_code`, `audited`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;


-- ----------------------------
-- Table structure for sys_log_archive
-- ----------------------------
CREATE TABLE `sys_log_archive` (
  `id` varchar (32) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `log_file` longblob DEFAULT NULL,
  `file_size` int DEFAULT NULL,
  `archive_time` varchar(255) DEFAULT NULL,
  `archive_cnt` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;


-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
CREATE TABLE `sys_job` (
  `id` varchar (32) NOT NULL,
  `job_name` varchar (255) DEFAULT NULL,
  `job_class` varchar (255) DEFAULT NULL,
  `job_group_key` varchar (255) DEFAULT NULL,
  `job_key` varchar (255) DEFAULT NULL,
  `tk_group_key` varchar (255) DEFAULT NULL,
  `tk_key` varchar (255) DEFAULT NULL,
  `cron` varchar (255) DEFAULT NULL,
  `started` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_job`(`job_class`) USING BTREE,
  UNIQUE INDEX `unique_job_key`(`job_group_key`, `job_key`) USING BTREE,
  UNIQUE INDEX `unique_tk_key`(`tk_group_key`, `tk_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;


SET FOREIGN_KEY_CHECKS = 1;
