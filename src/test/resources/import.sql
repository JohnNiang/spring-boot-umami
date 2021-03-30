-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: umami
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `account`
--

/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`user_id`, `username`, `password`, `is_admin`, `created_at`, `updated_at`) VALUES (1,'admin','$2b$10$BUli0c.muyCW1ErNJc3jL.vFRFtFJWrT8/GcR4A.sUdCznaXiqFXa',1,'2021-03-28 09:24:24','2021-03-28 09:24:24');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

--
-- Dumping data for table `event`
--

/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;

--
-- Dumping data for table `website`
--

/*!40000 ALTER TABLE `website` DISABLE KEYS */;
INSERT INTO `website` (`website_id`, `website_uuid`, `user_id`, `name`, `domain`, `share_id`, `created_at`) VALUES (1,'0b719afc-1fed-4b90-8615-5f96b1c0161c',1,'JohnNiangs Blog','johnniang.me',NULL,'2021-03-28 09:48:10');
/*!40000 ALTER TABLE `website` ENABLE KEYS */;

--
-- Dumping data for table `session`
--

/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` (`session_id`, `session_uuid`, `website_id`, `created_at`, `hostname`, `browser`, `os`, `device`, `screen`, `language`, `country`) VALUES (1,'e41739da-fed7-589c-a60c-7e97591f6463',1,'2021-03-29 00:57:05','localhost','chrome','Linux','desktop','2560x1440','en-US',NULL),(2,'46c493e2-e959-5cbf-9c66-104b0e86e216',1,'2021-03-29 01:30:14','localhost','firefox','Linux','desktop','2560x1440','en-US',NULL);
/*!40000 ALTER TABLE `session` ENABLE KEYS */;

--
-- Dumping data for table `pageview`
--

/*!40000 ALTER TABLE `pageview` DISABLE KEYS */;
INSERT INTO `pageview` (`view_id`, `website_id`, `session_id`, `created_at`, `url`, `referrer`) VALUES (1,1,1,'2021-03-29 00:57:05','/','http://localhost:8090/admin/index.html'),(2,1,1,'2021-03-29 00:57:13','/archives/hello-halo','http://localhost:8090/'),(3,1,1,'2021-03-29 00:57:25','/categories/default','http://localhost:8090/archives/hello-halo'),(4,1,1,'2021-03-29 00:57:26','/s/about','http://localhost:8090/categories/default'),(5,1,1,'2021-03-29 01:07:34','/archives','http://localhost:8090/s/about'),(6,1,1,'2021-03-29 01:08:47','/s/about','http://localhost:8090/archives'),(7,1,1,'2021-03-29 01:08:48','/categories/default','http://localhost:8090/s/about'),(8,1,1,'2021-03-29 01:08:49','/','http://localhost:8090/categories/default'),(9,1,1,'2021-03-29 01:08:51','/archives/hello-halo','http://localhost:8090/'),(10,1,1,'2021-03-29 01:08:57','/','http://localhost:8090/archives/hello-halo'),(11,1,1,'2021-03-29 01:10:45','/','http://localhost:8090/archives/hello-halo'),(12,1,1,'2021-03-29 01:10:46','/','http://localhost:8090/archives/hello-halo'),(13,1,1,'2021-03-29 01:11:24','/archives','http://localhost:8090/'),(14,1,1,'2021-03-29 01:11:26','/categories/default','http://localhost:8090/archives'),(15,1,1,'2021-03-29 01:11:27','/s/about','http://localhost:8090/categories/default'),(16,1,1,'2021-03-29 01:11:32','/search?keyword=a','http://localhost:8090/s/about'),(17,1,1,'2021-03-29 01:11:35','/categories/default','http://localhost:8090/search?keyword=a'),(18,1,1,'2021-03-29 01:11:37','/archives','http://localhost:8090/categories/default'),(19,1,1,'2021-03-29 01:11:38','/s/about','http://localhost:8090/archives'),(20,1,1,'2021-03-29 01:11:39','/','http://localhost:8090/s/about'),(21,1,1,'2021-03-29 01:11:42','/','http://localhost:8090/'),(22,1,1,'2021-03-29 01:12:25','/search?keyword=b','http://localhost:8090/'),(23,1,1,'2021-03-29 01:12:56','/archives/hello-halo','http://localhost:8090/search?keyword=b'),(24,1,2,'2021-03-29 01:30:14','/archives/hello-halo',''),(25,1,2,'2021-03-29 01:30:16','/categories/default','http://localhost:8090/archives/hello-halo'),(26,1,2,'2021-03-29 01:30:17','/s/about','http://localhost:8090/categories/default'),(27,1,2,'2021-03-29 01:30:18','/','http://localhost:8090/s/about'),(28,1,1,'2021-03-29 15:36:55','/','');
/*!40000 ALTER TABLE `pageview` ENABLE KEYS */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-30 12:17:23
