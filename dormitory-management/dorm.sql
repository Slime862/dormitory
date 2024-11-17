/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : dorm

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2022-07-30 23:28:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dorm
-- ----------------------------
DROP TABLE IF EXISTS `dorm`;
CREATE TABLE `dorm` (
  `id` varchar(32) NOT NULL COMMENT '宿舍号',
  `dormname` varchar(32) NOT NULL COMMENT '宿舍楼名',
  `dormno` varchar(10) NOT NULL COMMENT '宿舍楼号',
  `roomno` varchar(5) NOT NULL COMMENT '房间号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dormno` (`dormno`) USING BTREE,
  KEY `dormname` (`dormname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='宿舍楼信息表';

-- ----------------------------
-- Records of dorm
-- ----------------------------
INSERT INTO `dorm` VALUES ('A1-101', 'A', '1', '101');
INSERT INTO `dorm` VALUES ('A1-102', 'A', '1', '102');
INSERT INTO `dorm` VALUES ('A1-103', 'A', '1', '103');
INSERT INTO `dorm` VALUES ('A1-104', 'A', '1', '104');
INSERT INTO `dorm` VALUES ('A1-105', 'A', '1', '105');
INSERT INTO `dorm` VALUES ('A2-101', 'A', '2', '101');
INSERT INTO `dorm` VALUES ('A2-102', 'A', '2', '102');
INSERT INTO `dorm` VALUES ('A2-103', 'A', '2', '103');
INSERT INTO `dorm` VALUES ('A2-104', 'A', '2', '104');
INSERT INTO `dorm` VALUES ('A2-105', 'A', '2', '105');
INSERT INTO `dorm` VALUES ('B1-101', 'B', '1', '101');
INSERT INTO `dorm` VALUES ('B1-102', 'B', '1', '102');
INSERT INTO `dorm` VALUES ('B1-103', 'B', '1', '103');
INSERT INTO `dorm` VALUES ('B1-104', 'B', '1', '104');
INSERT INTO `dorm` VALUES ('B1-105', 'B', '1', '105');
INSERT INTO `dorm` VALUES ('B2-101', 'B', '2', '101');
INSERT INTO `dorm` VALUES ('B2-102', 'B', '2', '102');
INSERT INTO `dorm` VALUES ('B2-103', 'B', '2', '103');
INSERT INTO `dorm` VALUES ('B2-104', 'B', '2', '104');
INSERT INTO `dorm` VALUES ('B2-105', 'B', '2', '105');
INSERT INTO `dorm` VALUES ('C1-101', 'C', '1', '101');
INSERT INTO `dorm` VALUES ('E2-111', 'E', '2', '111');

-- ----------------------------
-- Table structure for external
-- ----------------------------
DROP TABLE IF EXISTS `external`;
CREATE TABLE `external` (
  `id` varchar(32) NOT NULL,
  `name` varchar(10) NOT NULL,
  `sex` int(2) NOT NULL COMMENT '0男1女',
  `phone` varchar(11) NOT NULL,
  `stuname` varchar(10) NOT NULL COMMENT '被访学生姓名',
  `visittime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '来访时间',
  `endtime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `dormname` varchar(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='外来人员访问表';

-- ----------------------------
-- Records of external
-- ----------------------------
INSERT INTO `external` VALUES ('40288a8c824f99dc01824fb594ef0016', '冰墩墩', '0', '18209519200', '石佛寺', '2022-07-30 15:24:36', '2022-07-30 15:24:40', 'A1');
INSERT INTO `external` VALUES ('40288a8c824f99dc01824fb5c1810017', '冰墩墩', '0', '18209519200', '石佛寺', '2022-07-30 15:24:48', '2022-07-30 15:24:49', 'A1');

-- ----------------------------
-- Table structure for inregister
-- ----------------------------
DROP TABLE IF EXISTS `inregister`;
CREATE TABLE `inregister` (
  `id` varchar(32) NOT NULL COMMENT '编号',
  `stuid` varchar(32) NOT NULL COMMENT '学生id',
  `staffid` varchar(32) NOT NULL COMMENT '管理员id',
  `rebatchid` varchar(32) NOT NULL COMMENT '登记时间批次',
  `arrivetime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '到校时间',
  `status` varchar(2) NOT NULL COMMENT '状态，0 创建 1 填写完毕 2 历史',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `stuid` (`stuid`) USING BTREE,
  CONSTRAINT `inregister_ibfk_1` FOREIGN KEY (`stuid`) REFERENCES `stuinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='入校离校登记表';

-- ----------------------------
-- Records of inregister
-- ----------------------------
INSERT INTO `inregister` VALUES ('40288a8c824f0b9001824f1a45bd0010', '20151001', '1001', '40288a8c824f0b9001824f1a45bd000f', '2022-01-01 00:00:00', '2');
INSERT INTO `inregister` VALUES ('40288a8c824f0b9001824f1b64030012', '20151001', '1001', '40288a8c824f0b9001824f1b64030011', '2222-01-01 00:00:00', '2');
INSERT INTO `inregister` VALUES ('40288a8c824f99dc01824faa29f8000a', '20151001', '1001', '40288a8c824f99dc01824faa29f60009', '2022-01-01 00:00:00', '2');
INSERT INTO `inregister` VALUES ('40288a8c824f99dc01824fb07cb8000e', '20151001', '1001', '40288a8c824f99dc01824fb07cb8000d', '2022-10-11 00:00:00', '2');
INSERT INTO `inregister` VALUES ('40288a8c824f99dc01824fb4effd0014', '20151001', '1001', '40288a8c824f99dc01824fb4effd0013', null, '0');

-- ----------------------------
-- Table structure for outregister
-- ----------------------------
DROP TABLE IF EXISTS `outregister`;
CREATE TABLE `outregister` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `stuid` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '学生id',
  `staffid` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '管理员id',
  `rebatchid` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '登记批次id',
  `leavetime` datetime DEFAULT NULL COMMENT '离校时间',
  `city` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '去向',
  `status` varchar(2) COLLATE utf8mb4_bin NOT NULL COMMENT '状态，0 创建 1 历史',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of outregister
-- ----------------------------
INSERT INTO `outregister` VALUES ('40288a8c824f99dc01824fb1318b0010', '20151001', '1001', '40288a8c824f99dc01824fb13189000f', null, null, '0');

-- ----------------------------
-- Table structure for registerbatch
-- ----------------------------
DROP TABLE IF EXISTS `registerbatch`;
CREATE TABLE `registerbatch` (
  `id` varchar(32) NOT NULL COMMENT '登记批次编号',
  `name` varchar(32) NOT NULL COMMENT '登记批次名称',
  `staffid` varchar(32) NOT NULL COMMENT '管理员id',
  `status` varchar(5) NOT NULL COMMENT '入校、离校标识 in out',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='登记批次表';

-- ----------------------------
-- Records of registerbatch
-- ----------------------------
INSERT INTO `registerbatch` VALUES ('40288a8c824f0b9001824f1a45bd000f', '测试账号', '1001', 'in');
INSERT INTO `registerbatch` VALUES ('40288a8c824f0b9001824f1b64030011', '国庆放假回来登记', '1001', 'in');
INSERT INTO `registerbatch` VALUES ('40288a8c824f99dc01824faa29f60009', '国庆回校时间登记', '1001', 'in');
INSERT INTO `registerbatch` VALUES ('40288a8c824f99dc01824fb07cb8000d', '新学期返校时间', '1001', 'in');
INSERT INTO `registerbatch` VALUES ('40288a8c824f99dc01824fb13189000f', '中秋离校时间', '1001', 'out');
INSERT INTO `registerbatch` VALUES ('40288a8c824f99dc01824fb4effd0013', '中秋返校时间', '1001', 'in');

-- ----------------------------
-- Table structure for repair
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair` (
  `id` varchar(32) NOT NULL COMMENT '报修单编号',
  `dormid` varchar(32) NOT NULL COMMENT '宿舍号',
  `stuid` varchar(32) NOT NULL COMMENT '学生id',
  `reason` int(2) NOT NULL COMMENT '报修类型:0灯1床，桌椅2门窗3其他',
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '报修日期',
  `note` varchar(255) DEFAULT NULL COMMENT '说明',
  `status` int(2) NOT NULL COMMENT '0创建1处理中2完成',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dormid` (`dormid`) USING BTREE,
  KEY `stuid` (`stuid`) USING BTREE,
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`dormid`) REFERENCES `dorm` (`id`),
  CONSTRAINT `repair_ibfk_2` FOREIGN KEY (`stuid`) REFERENCES `stuinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='报修单';

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES ('1', 'A1-103', '20151001', '0', '2019-06-13 00:09:51', 'xxx', '2');
INSERT INTO `repair` VALUES ('40288a8c824f0b9001824f0f7295000c', 'A1-101', '20151001', '0', '2022-07-30 12:23:09', '测试', '1');
INSERT INTO `repair` VALUES ('40288a8c824f0b9001824f0f8d90000d', 'A1-101', '20151001', '2', '2022-07-30 12:23:16', '测下', '2');
INSERT INTO `repair` VALUES ('40288a8c824f0b9001824f0fa860000e', 'A1-101', '20151001', '2', '2022-07-30 12:23:22', '好嘞', '0');
INSERT INTO `repair` VALUES ('40288a8c824f99dc01824fb68adc0018', 'A1-101', '20151001', '3', '2022-07-30 15:25:39', '测试', '0');

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` varchar(32) NOT NULL COMMENT '编号',
  `batchid` varchar(32) NOT NULL COMMENT '评分批次编号',
  `dormid` varchar(32) NOT NULL COMMENT '宿舍号',
  `sanitary` double(4,2) DEFAULT NULL COMMENT '整齐度',
  `tidy` double(4,2) DEFAULT NULL COMMENT '卫生',
  `sum` double(4,2) DEFAULT NULL COMMENT '总分',
  `orderScore` varchar(255) DEFAULT NULL COMMENT '排名',
  `status` varchar(2) NOT NULL COMMENT '状态，0 创建 1创建完成 2 历史',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dormid` (`dormid`) USING BTREE,
  KEY `batchid` (`batchid`) USING BTREE,
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`dormid`) REFERENCES `dorm` (`id`),
  CONSTRAINT `score_ibfk_2` FOREIGN KEY (`batchid`) REFERENCES `scoringbatch` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='宿舍评分表';

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES ('40288a8c824f0b9001824f0d05f80001', '40288a8c824f0b9001824f0d05ed0000', 'A1-102', null, null, '2.00', '1', '1');
INSERT INTO `score` VALUES ('40288a8c824f0b9001824f0d05f80002', '40288a8c824f0b9001824f0d05ed0000', 'A1-103', null, null, '2.00', '2', '1');
INSERT INTO `score` VALUES ('40288a8c824f0b9001824f0d05f90003', '40288a8c824f0b9001824f0d05ed0000', 'A1-104', null, null, '2.00', '3', '1');
INSERT INTO `score` VALUES ('40288a8c824f99dc01824facbfb2000c', '40288a8c824f99dc01824facbfa9000b', 'A1-101', '1.00', '2.00', '3.00', '1', '1');
INSERT INTO `score` VALUES ('4028b8816b2cdb99016b2d5ea30d0010', '4028b8816b2cdb99016b2d5ea305000f', 'A1-101', '34.56', '42.37', '76.93', '4', '2');
INSERT INTO `score` VALUES ('4028b8816b2cdb99016b2d5ea30d0011', '4028b8816b2cdb99016b2d5ea305000f', 'A1-102', '35.56', '43.37', '78.93', '3', '2');
INSERT INTO `score` VALUES ('4028b8816b2cdb99016b2d5ea30e0012', '4028b8816b2cdb99016b2d5ea305000f', 'A1-103', '36.56', '44.37', '80.93', '2', '2');
INSERT INTO `score` VALUES ('4028b8816b2cdb99016b2d5ea30e0013', '4028b8816b2cdb99016b2d5ea305000f', 'A1-104', '37.56', '45.37', '82.93', '1', '2');
INSERT INTO `score` VALUES ('4028b8816b2d8537016b2d87c6a70001', '4028b8816b2d8537016b2d87c6800000', 'A1-101', '46.33', '47.34', '93.67', '4', '2');
INSERT INTO `score` VALUES ('4028b8816b2d8537016b2d87c6a90002', '4028b8816b2d8537016b2d87c6800000', 'A1-102', '47.33', '48.34', '95.67', '3', '2');
INSERT INTO `score` VALUES ('4028b8816b2d8537016b2d87c6a90003', '4028b8816b2d8537016b2d87c6800000', 'A1-103', '48.33', '49.34', '97.67', '2', '2');
INSERT INTO `score` VALUES ('4028b8816b2d8537016b2d87c6a90004', '4028b8816b2d8537016b2d87c6800000', 'A1-104', '49.33', '50.34', '99.67', '1', '2');

-- ----------------------------
-- Table structure for scoringbatch
-- ----------------------------
DROP TABLE IF EXISTS `scoringbatch`;
CREATE TABLE `scoringbatch` (
  `id` varchar(32) NOT NULL DEFAULT '批次编号',
  `name` varchar(32) NOT NULL DEFAULT '评分批次名称',
  `dormname` varchar(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='评分批次表';

-- ----------------------------
-- Records of scoringbatch
-- ----------------------------
INSERT INTO `scoringbatch` VALUES ('40288a8c824f0b9001824f0d05ed0000', '测试', 'A1');
INSERT INTO `scoringbatch` VALUES ('40288a8c824f99dc01824facbfa9000b', '测试', 'A1');
INSERT INTO `scoringbatch` VALUES ('4028b8816b2cdb99016b2d5ea305000f', '期中检查', 'A1');
INSERT INTO `scoringbatch` VALUES ('4028b8816b2d8537016b2d87c6800000', '期末检查', 'A1');

-- ----------------------------
-- Table structure for staffinfo
-- ----------------------------
DROP TABLE IF EXISTS `staffinfo`;
CREATE TABLE `staffinfo` (
  `id` varchar(32) NOT NULL COMMENT '职工编号',
  `staffname` varchar(10) NOT NULL COMMENT '姓名',
  `sex` int(2) NOT NULL COMMENT '0男1女',
  `age` int(3) NOT NULL,
  `dormname` varchar(10) NOT NULL COMMENT '宿舍楼名',
  `dormno` varchar(5) NOT NULL COMMENT '宿舍楼号',
  `phone` varchar(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `staffinfo_ibfk_1` (`dormname`) USING BTREE,
  KEY `staffinfo_ibfk_2` (`dormno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='职工信息表';

-- ----------------------------
-- Records of staffinfo
-- ----------------------------
INSERT INTO `staffinfo` VALUES ('1001', '李冬', '1', '40', 'A', '1', '18779326744');
INSERT INTO `staffinfo` VALUES ('1002', '林芳叶', '1', '42', 'A', '2', '15573679087');
INSERT INTO `staffinfo` VALUES ('1003', '张丹', '1', '45', 'B', '1', '17365711897');
INSERT INTO `staffinfo` VALUES ('1004', '黄晓云', '1', '39', 'B', '2', '15703398489');

-- ----------------------------
-- Table structure for stuinfo
-- ----------------------------
DROP TABLE IF EXISTS `stuinfo`;
CREATE TABLE `stuinfo` (
  `id` varchar(32) NOT NULL COMMENT '学号',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `sex` int(2) NOT NULL COMMENT '0男1女',
  `grade` varchar(4) NOT NULL COMMENT '年级',
  `academy` varchar(32) NOT NULL COMMENT '学院',
  `major` varchar(32) NOT NULL COMMENT '专业',
  `dormid` varchar(32) NOT NULL COMMENT '宿舍号',
  `phone` varchar(11) NOT NULL COMMENT '电话号码',
  `status` int(2) NOT NULL COMMENT '0在校1离校',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dormid` (`dormid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='学生信息表';

-- ----------------------------
-- Records of stuinfo
-- ----------------------------
INSERT INTO `stuinfo` VALUES ('20151001', '石佛寺', '0', '2015', '管理学院', '工商管理', 'A1-101', '13487298743', '0');
INSERT INTO `stuinfo` VALUES ('2022001', '王美丽', '1', '2022', '计算机学院', '计算机', 'E1-111', '13666666666', '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `permission` int(2) NOT NULL COMMENT '0学生1宿管2系统管理员',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userid` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户登录信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', '2');
INSERT INTO `user` VALUES ('4028aba26afeb7fb016afed608900000', '1001', '123456', '1');
INSERT INTO `user` VALUES ('4028aba26afeb7fb016afed608910001', '1002', '123456', '1');
INSERT INTO `user` VALUES ('4028aba26afeb7fb016afed608910002', '1003', '123456', '1');
INSERT INTO `user` VALUES ('4028aba26afeb7fb016afed608910003', '1004', '123456', '1');
INSERT INTO `user` VALUES ('4028aba26afeb7fb016afed6762f0004', '20151001', '123456', '0');
