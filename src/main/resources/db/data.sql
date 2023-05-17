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
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$HAe6gTZo.uxxFEDrjU74GOk0nPc9bZoTOxWAgI0HUOH/R49.PiHXa', 'L', '1', '2023-01-01 00:00:00', '2023-01-01 00:00:00', '2023-01-01 00:00:00', 1, 1, 1, 1);
INSERT INTO `sys_user` VALUES ('2', 'operator', '$2a$10$.ugWvLvv0DWOkYE6FvAoQuAg/LBEjkIEdGBLwdu9q3gky00hxzLhC', 'L', '2', '2023-01-01 00:00:00', '2023-01-01 00:00:00', '2023-01-01 00:00:00', 1, 1, 1, 1);
INSERT INTO `sys_user` VALUES ('3', 'auditor', '$2a$10$MJKrF9eOy7nIXnEIopxCtuf4lTyNKlieLKcIAxJP12XTroiJmHGHa', 'L', '3', '2023-01-01 00:00:00', '2023-01-01 00:00:00', '2023-01-01 00:00:00', 1, 1, 1, 1);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'ROLE_admin', 1, 'ROLE_admin', '2023-01-01 00:00:00');
INSERT INTO `sys_role` VALUES ('2', '操作员', 'ROLE_operator', 2, 'ROLE_admin', '2023-01-01 00:00:00');
INSERT INTO `sys_role` VALUES ('3', '审计员', 'ROLE_auditor', 2, 'ROLE_admin', '2023-01-01 00:00:00');

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '0', 'M', '首页', 'index', 0, 0);
INSERT INTO `sys_permission` VALUES ('2', '1', 'P', '查询', 'index:select', 0, 0);
INSERT INTO `sys_permission` VALUES ('100', '0', 'D', '系统管理', 'system', 0, 0);
INSERT INTO `sys_permission` VALUES ('110', '100', 'M', '用户管理', 'system:user', 0, 0);
INSERT INTO `sys_permission` VALUES ('111', '110', 'P', '查询', 'system:user:select', 0, 0);
INSERT INTO `sys_permission` VALUES ('112', '110', 'P', '查询', 'system:user:add/edit', 0, 0);

INSERT INTO `sys_permission` VALUES ('121', '0', 'P', '查询', 'system:role:select', 0, 0);

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '1', '2');
INSERT INTO `sys_role_permission` VALUES ('100', '1', '100');
INSERT INTO `sys_role_permission` VALUES ('110', '1', '110');
INSERT INTO `sys_role_permission` VALUES ('111', '1', '111');
INSERT INTO `sys_role_permission` VALUES ('112', '1', '112');
INSERT INTO `sys_role_permission` VALUES ('121', '1', '121');

SET FOREIGN_KEY_CHECKS = 1;
