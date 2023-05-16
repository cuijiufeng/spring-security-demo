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
-- Table structure for sys_permission
-- ----------------------------
CREATE TABLE `sys_permission`  (
  `id` varchar(32) NOT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `type` varchar(1) DEFAULT NULL COMMENT 'D:目录 M:菜单 P:权限',
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `required` tinyint DEFAULT NULL,
  `menu_hidden` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
CREATE TABLE `sys_role`  (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `role_key` varchar(255) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `level` int DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) NOT NULL,
  `r_id` varchar(32) DEFAULT NULL,
  `p_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE `sys_user`  (
  `id` varchar(32) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_intensity` int DEFAULT NULL,
  `role_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_update_password_time` datetime DEFAULT NULL,
  `account_non_expired` tinyint DEFAULT NULL,
  `credentials_non_expired` tinyint DEFAULT NULL,
  `account_non_locked` tinyint DEFAULT NULL,
  `enabled` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
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
  `opt_time` datetime DEFAULT NULL,
  `mac` varchar(64) DEFAULT NULL,
  `audited` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
