-- MySQL dump 10.19  Distrib 10.3.30-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	10.3.30-MariaDB-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `student_name` varchar(20) NOT NULL,
  `school_name` varchar(20) NOT NULL,
  `school_level` varchar(10) NOT NULL,
  `school_grade` tinyint(1) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT NULL,
  `student_gender` varchar(2) DEFAULT NULL,
  `student_age` int(11) DEFAULT NULL,
  `is_enrolled` tinyint(4) DEFAULT NULL,
  `enrolled_date` date DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (18,'홍길동','활빈','고등학교',2,NULL,'남자',16,1,'2018-02-01'),(19,'허균','활빈','고등학교',2,NULL,'남자',32,1,'2018-02-01'),(20,'연산군','조선','고등학교',3,NULL,'남자',36,0,'2017-05-28'),(21,'세종','조선','고등학교',3,NULL,'남자',42,1,'2017-06-01'),(22,'홍상직','조선','고등학교',2,NULL,'남자',40,0,'2018-05-04'),(23,'성춘향','남원','중학교',3,NULL,'여자',15,1,'2019-02-16'),(24,'이몽룡','남원','고등학교',1,NULL,'남자',17,1,'2018-04-15'),(25,'방자','남원','중학교',2,NULL,'남자',18,1,'2018-11-21'),(26,'향단','남원','중학교',2,NULL,'여자',16,1,'2019-03-03'),(27,'변사또','남원','고등학교',3,NULL,'남자',38,1,'2017-01-19'),(28,'심청','도화','중학교',2,NULL,'여자',15,1,'2019-02-01'),(29,'심학규','도화','고등학교',3,NULL,'남자',33,1,'2018-04-07'),(30,'용왕','도화','고등학교',3,NULL,'남자',299,0,'2017-08-04'),(31,'곽씨','도화','고등학교',2,NULL,'여자',31,1,'2018-05-29'),(32,'뺑덕','도화','고등학교',1,NULL,'여자',33,1,'2018-09-12'),(33,'흥부','남원','고등학교',2,NULL,'남자',35,1,'2017-06-11'),(34,'놀부','남원','고등학교',3,NULL,'남자',36,1,'2017-06-11');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tests` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `student_name` varchar(20) DEFAULT NULL,
  `is_taken` tinyint(1) DEFAULT NULL,
  `test_date` date DEFAULT NULL,
  `korean` int(10) unsigned DEFAULT NULL,
  `english` int(10) unsigned DEFAULT NULL,
  `math` int(10) unsigned DEFAULT NULL,
  `history` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests`
--

LOCK TABLES `tests` WRITE;
/*!40000 ALTER TABLE `tests` DISABLE KEYS */;
INSERT INTO `tests` VALUES (1,'홍길동',1,'2019-12-11',65,83,43,100),(2,'허균',0,'2019-12-11',0,0,0,0),(3,'연산군',0,'2019-12-11',0,0,0,0),(4,'세종',1,'2019-12-11',100,67,88,100),(5,'홍상직',1,'2019-12-11',67,67,60,78),(6,'성춘향',1,'2019-12-11',96,100,94,90),(7,'이몽룡',1,'2019-12-11',100,96,98,100),(8,'방자',1,'2019-12-11',80,50,40,60),(9,'향단',1,'2019-12-11',78,77,64,40),(10,'변사또',1,'2019-12-11',82,88,78,100),(11,'심천',1,'2019-12-11',93,82,84,98),(12,'심학규',0,'2019-12-11',0,0,0,0),(13,'용왕',1,'2019-12-11',100,100,100,100),(14,'곽씨',1,'2019-12-11',100,88,98,100),(15,'뺑덕',1,'2019-12-11',45,33,42,40),(16,'흥부',1,'2019-12-11',83,79,100,90),(17,'놀부',1,'2019-12-11',98,100,88,94),(18,'현진건',1,'2019-12-11',100,96,72,100),(19,'염상섭',1,'2019-12-11',100,100,71,96),(20,'신채호',1,'2019-12-11',100,98,100,100);
/*!40000 ALTER TABLE `tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tuitions`
--

DROP TABLE IF EXISTS `tuitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tuitions` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `student_name` varchar(20) DEFAULT NULL,
  `is_paid` tinyint(1) DEFAULT NULL,
  `due_year` int(11) DEFAULT NULL,
  `due_month` int(11) DEFAULT NULL,
  `due_day` int(11) DEFAULT NULL,
  `due_amount` int(11) DEFAULT NULL,
  `memo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tuitions`
--

LOCK TABLES `tuitions` WRITE;
/*!40000 ALTER TABLE `tuitions` DISABLE KEYS */;
INSERT INTO `tuitions` VALUES (1,'홍길동',1,2019,11,1,300000,''),(2,'허균',1,2019,11,2,300000,''),(3,'연산군',1,2019,11,3,150000,'교직원 할인'),(4,'세종',1,2019,11,3,150000,'교직원 할인'),(5,'흥상직',0,2019,11,5,300000,''),(6,'성춘향',0,2019,11,11,300000,''),(7,'이몽룡',1,2019,11,15,300000,''),(8,'변사또',1,2019,11,18,300000,'상습체납자'),(9,'심청',0,2019,11,21,150000,'사회보호대상자'),(10,'심학규',1,2019,11,21,150000,'사회보호대상자'),(11,'놀부',1,2019,11,30,300000,''),(12,'신채호',1,2019,11,30,150000,'국가유공자');
/*!40000 ALTER TABLE `tuitions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-17 17:39:08
