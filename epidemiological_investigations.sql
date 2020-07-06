/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100413
 Source Host           : localhost:3306
 Source Schema         : epidemiological_investigations

 Target Server Type    : MySQL
 Target Server Version : 100413
 File Encoding         : 65001

 Date: 06/07/2020 14:51:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comuni
-- ----------------------------
DROP TABLE IF EXISTS `comuni`;
CREATE TABLE `comuni`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `superficie` decimal(12, 2) NOT NULL,
  `istat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `data_istituzione` date NOT NULL,
  `territorio` int(1) NOT NULL,
  `mare` tinyint(1) NOT NULL,
  `id_provincia` int(10) NOT NULL,
  `id_responsabile` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_provincia`(`id_provincia`) USING BTREE,
  INDEX `id_responsabile`(`id_responsabile`) USING BTREE,
  CONSTRAINT `id_provincia` FOREIGN KEY (`id_provincia`) REFERENCES `province` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `id_responsabile` FOREIGN KEY (`id_responsabile`) REFERENCES `utenti` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contagi
-- ----------------------------
DROP TABLE IF EXISTS `contagi`;
CREATE TABLE `contagi`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `malattia` int(2) NOT NULL,
  `persone_ricoverate` int(10) NOT NULL,
  `persone_in_cura` int(10) NOT NULL,
  `id_segnalazione` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_segnalazione`(`id_segnalazione`) USING BTREE,
  CONSTRAINT `id_segnalazione` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_contagi` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for decessi
-- ----------------------------
DROP TABLE IF EXISTS `decessi`;
CREATE TABLE `decessi`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `causa` int(2) NOT NULL,
  `numero` int(10) NOT NULL,
  `id_segnalazione` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_segnalazione`(`id_segnalazione`) USING BTREE,
  CONSTRAINT `decessi_ibfk_1` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_decessi` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `superficie` decimal(12, 2) NOT NULL,
  `capoluogo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_regione` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `nome`(`nome`) USING BTREE,
  INDEX `id_regione`(`id_regione`) USING BTREE,
  CONSTRAINT `province_ibfk_1` FOREIGN KEY (`id_regione`) REFERENCES `regioni` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for regioni
-- ----------------------------
DROP TABLE IF EXISTS `regioni`;
CREATE TABLE `regioni`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `superficie` decimal(12, 2) NOT NULL,
  `capoluogo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `nome`(`nome`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for segnalazioni_contagi
-- ----------------------------
DROP TABLE IF EXISTS `segnalazioni_contagi`;
CREATE TABLE `segnalazioni_contagi`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `id_comune` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_comune`(`id_comune`) USING BTREE,
  CONSTRAINT `id_comune` FOREIGN KEY (`id_comune`) REFERENCES `comuni` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for segnalazioni_decessi
-- ----------------------------
DROP TABLE IF EXISTS `segnalazioni_decessi`;
CREATE TABLE `segnalazioni_decessi`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `id_provincia` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_provincia`(`id_provincia`) USING BTREE,
  CONSTRAINT `segnalazioni_decessi_ibfk_1` FOREIGN KEY (`id_provincia`) REFERENCES `province` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for utenti
-- ----------------------------
DROP TABLE IF EXISTS `utenti`;
CREATE TABLE `utenti`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cognome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ruolo` int(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of utenti
-- ----------------------------
INSERT INTO `utenti` VALUES (1, 'admin', 'admin', 'admin', '-7396891a4abefbea4216f742b211ea204e9856378c03b44757e090d54bb756e8', 0);

SET FOREIGN_KEY_CHECKS = 1;
