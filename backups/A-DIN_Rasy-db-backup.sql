-- MySQL dump 10.13  Distrib 8.4.9, for Linux (x86_64)
--
-- Host: mysql    Database: A-DIN_Rasy-db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `barcode_type` enum('CODE_128','EAN_13') DEFAULT NULL,
  `blood_group` varchar(60) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `department` varchar(80) DEFAULT NULL,
  `email` varchar(120) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `full_name` varchar(120) NOT NULL,
  `issue_date` date DEFAULT NULL,
  `phone` varchar(40) DEFAULT NULL,
  `photo_content_type` varchar(60) DEFAULT NULL,
  `photo_file_name` varchar(255) DEFAULT NULL,
  `registration_number` varchar(64) NOT NULL,
  `title` varchar(120) DEFAULT NULL,
  `type` enum('EMPLOYEE','STUDENT','USER') NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `template_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profile_reg_number` (`registration_number`),
  UNIQUE KEY `UKfetebim4s6b2yrqmjjwuakic9` (`uuid`),
  KEY `FK92ganbu1q1rhy9wdmquc37xr9` (`template_id`),
  CONSTRAINT `FK92ganbu1q1rhy9wdmquc37xr9` FOREIGN KEY (`template_id`) REFERENCES `templates` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'CODE_128','','2026-06-17 12:19:29.284395',NULL,'DevOps','rasy@test.com','2027-06-17','DIN Rasy Test Profile','2026-06-17','',NULL,NULL,'RASY-2026-TEST','Student','STUDENT','2026-06-17 12:19:29.284395','b6504290-6bd2-4734-9ade-8689f2db6dfa',1);
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `templates`
--

DROP TABLE IF EXISTS `templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `templates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(60) NOT NULL,
  `layout` varchar(20) NOT NULL,
  `name` varchar(80) NOT NULL,
  `organization_name` varchar(120) DEFAULT NULL,
  `primary_color` varchar(7) NOT NULL,
  `secondary_color` varchar(7) NOT NULL,
  `tagline` varchar(255) DEFAULT NULL,
  `text_color` varchar(7) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq29dtfrj97gkqt015sdyurqcm` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `templates`
--

LOCK TABLES `templates` WRITE;
/*!40000 ALTER TABLE `templates` DISABLE KEYS */;
INSERT INTO `templates` VALUES (1,'STUDENT_BLUE','VERTICAL','Student Blue Theme','National University','#1d4ed8','#e0e7ff','Excellence in Education','#111827'),(2,'EMPLOYEE_GREEN','HORIZONTAL','Employee Green Theme','Enterprise Corp','#047857','#d1fae5','Innovating for the Future','#111827'),(3,'USER_GRAY','VERTICAL','User Gray Theme','Community Hub','#374151','#f3f4f6','Connect & Collaborate','#111827');
/*!40000 ALTER TABLE `templates` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-18  1:39:12
