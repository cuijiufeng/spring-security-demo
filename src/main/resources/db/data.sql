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

 Date: 27/04/2023 10:27:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$HAe6gTZo.uxxFEDrjU74GOk0nPc9bZoTOxWAgI0HUOH/R49.PiHXa', 1, '1', '2023-01-01 00:00:00', '2023-01-01 00:00:00', '2023-01-01 00:00:00', 1, 1, 1, 1);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'ROLE_admin', NULL, 1, 'ROLE_admin', '2023-01-01 00:00:00');

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '', 'index', 'index', 'index', 'index', 1);
INSERT INTO `sys_permission` VALUES ('2', '', 'help', 'help', 'hello', 'hello', 1);
INSERT INTO `sys_permission` VALUES ('3', '', 'help', 'help', 'other', 'other', 0);

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '1', '2');
INSERT INTO `sys_role_permission` VALUES ('3', '1', '3');

SET FOREIGN_KEY_CHECKS = 1;
