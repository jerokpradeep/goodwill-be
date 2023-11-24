-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.8.3-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for marketwatch
CREATE DATABASE IF NOT EXISTS `marketwatch` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `marketwatch`;

-- Dumping structure for table marketwatch.hibernate_sequence
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;

-- Data exporting was unselected.

-- Dumping structure for table marketwatch.marketwatch
CREATE TABLE IF NOT EXISTS `marketwatch` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `USER_ID` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Marketwatch for an user';

-- Data exporting was unselected.

-- Dumping structure for table marketwatch.marketwatchmap
CREATE TABLE IF NOT EXISTS `marketwatchmap` (
  `ID` bigint(20) NOT NULL,
  `MARKETWATCH_ID` bigint(20) NOT NULL,
  `SCRIP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MW_KEY_1` (`MARKETWATCH_ID`),
  KEY `SCR_KEY_1` (`SCRIP_ID`),
  CONSTRAINT `MW_KEY_1` FOREIGN KEY (`MARKETWATCH_ID`) REFERENCES `marketwatch` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SCR_KEY_1` FOREIGN KEY (`SCRIP_ID`) REFERENCES `scrips` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Mapping table between marketwatch and scrips';

-- Data exporting was unselected.

-- Dumping structure for table marketwatch.scrips
CREATE TABLE IF NOT EXISTS `scrips` (
  `ID` bigint(20) NOT NULL,
  `SYMBOL` varchar(50) NOT NULL DEFAULT '',
  `NAME` varchar(50) DEFAULT NULL,
  `EXCHANGE` varchar(50) NOT NULL,
  `SEGMENT` varchar(50) DEFAULT NULL,
  `EXPIRY` date DEFAULT NULL,
  `PREVIOUS_CLOSE` float DEFAULT NULL,
  `PREFERRED_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Marketwatch scrips details';

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
