CREATE DATABASE  IF NOT EXISTS `pay_my_buddy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pay_my_buddy`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: pay_my_buddy
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `monnaie`
--

DROP TABLE IF EXISTS `monnaie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monnaie` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monnaie`
--

LOCK TABLES `monnaie` WRITE;
/*!40000 ALTER TABLE `monnaie` DISABLE KEYS */;
INSERT INTO `monnaie` VALUES (1,'euro');
/*!40000 ALTER TABLE `monnaie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `solde` decimal(10,2) NOT NULL DEFAULT '0.00',
  `iban` varchar(34) NOT NULL,
  `monnaie_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `monnaie_id` (`monnaie_id`),
  CONSTRAINT `utilisateur_ibfk_1` FOREIGN KEY (`monnaie_id`) REFERENCES `monnaie` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

LOCK TABLES `utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` VALUES (7,'email01','mdp01',0.00,'rib01',0),(8,'email02','mdp02',0.00,'rib02',0);
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxe`
--

DROP TABLE IF EXISTS `taxe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taxe` (
  `id` int NOT NULL AUTO_INCREMENT,
  `montant` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxe`
--

LOCK TABLES `taxe` WRITE;
/*!40000 ALTER TABLE `taxe` DISABLE KEYS */;
INSERT INTO `taxe` VALUES (1,0.05);
/*!40000 ALTER TABLE `taxe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `montant` decimal(10,2) NOT NULL,
  `iban` varchar(34) DEFAULT NULL,
  `vers_compte` bit(1) DEFAULT NULL,
  `emetteur_id` int NOT NULL,
  `destinataire_id` int DEFAULT NULL,
  `taxe_id` int NOT NULL,
  `monnaie_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_transaction_emetteur_id` (`emetteur_id`),
  KEY `FK_transaction_taxe_id` (`taxe_id`),
  KEY `FK_transaction_destinataire_id` (`destinataire_id`),
  KEY `FK_transaction_monnaie_id` (`monnaie_id`),
  CONSTRAINT `FK_transaction_destinataire_id` FOREIGN KEY (`destinataire_id`) REFERENCES `utilisateur` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `FK_transaction_emetteur_id` FOREIGN KEY (`emetteur_id`) REFERENCES `utilisateur` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `FK_transaction_monnaie_id` FOREIGN KEY (`monnaie_id`) REFERENCES `monnaie` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `FK_transaction_taxe_id` FOREIGN KEY (`taxe_id`) REFERENCES `taxe` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur_utilisateur`
--

DROP TABLE IF EXISTS `utilisateur_utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur_utilisateur` (
  `FK_utilisateur_utilisateur_utilisateur_1_id` int NOT NULL,
  `FK_utilisateur_utilisateur_utilisateur_2_id` int NOT NULL,
  PRIMARY KEY (`FK_utilisateur_utilisateur_utilisateur_1_id`,`FK_utilisateur_utilisateur_utilisateur_2_id`),
  KEY `utilisateur_utilisateur_ibfk_2` (`FK_utilisateur_utilisateur_utilisateur_2_id`),
  CONSTRAINT `utilisateur_utilisateur_ibfk_1` FOREIGN KEY (`FK_utilisateur_utilisateur_utilisateur_1_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `utilisateur_utilisateur_ibfk_2` FOREIGN KEY (`FK_utilisateur_utilisateur_utilisateur_2_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur_utilisateur`
--

LOCK TABLES `utilisateur_utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur_utilisateur` DISABLE KEYS */;
/*!40000 ALTER TABLE `utilisateur_utilisateur` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-08 16:00:33
