-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: pay_my_buddy
-- ------------------------------------------------------
-- Server version	8.0.33

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
  `compte_bancaire` varchar(34) DEFAULT NULL,
  `vers_compte` tinyint(1) DEFAULT NULL,
  `emetteur_id` int NOT NULL,
  `destinataire_id` int DEFAULT NULL,
  `taxe_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `emetteur_id` (`emetteur_id`),
  KEY `taxe_id` (`taxe_id`),
  KEY `destinataire_id` (`destinataire_id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`emetteur_id`) REFERENCES `utilisateur` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `transaction_ibfk_3` FOREIGN KEY (`taxe_id`) REFERENCES `taxe` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `transaction_ibfk_4` FOREIGN KEY (`destinataire_id`) REFERENCES `utilisateur` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `CHK_transaction_montant_is_positive` CHECK ((`montant` >= 0)),
  CONSTRAINT `CHK_transaction_vers_compte` CHECK (((`compte_bancaire` is null) xor (`vers_compte` is not null)))
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (52,'2023-07-11 14:14:44','cancelled transaction',0.00,'ribpers1',1,1,NULL,1),(53,'2023-07-11 14:14:52','versement vers compte',10.00,'ribpers1',1,1,NULL,1),(54,'2023-07-11 14:15:12','versement vers solde',100.00,'ribpers1',0,1,NULL,1),(55,'2023-07-11 14:15:22','versement entre utilisateurs',20.00,NULL,NULL,1,2,1),(56,'2023-07-11 14:15:41','versement vers solde',100.00,'ribpers1',0,1,NULL,1),(57,'2023-07-11 14:15:41','versement vers compte',20.00,'ribpers1',1,1,NULL,1),(58,'2023-07-11 14:15:41','versement entre utilisateurs',20.00,NULL,NULL,1,2,1);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `transaction_BEFORE_INSERT` BEFORE INSERT ON `transaction` FOR EACH ROW BEGIN
IF (NEW.vers_compte IS NOT NULL)
THEN
	SET NEW.compte_bancaire = (SELECT compte_bancaire FROM utilisateur WHERE id = NEW.emetteur_id);
	CASE
		WHEN NEW.vers_compte = true 
        THEN
			CASE
				WHEN (SELECT solde FROM utilisateur WHERE id = NEW.emetteur_id) >= NEW.montant * (1 + (SELECT montant FROM taxe WHERE id=NEW.taxe_id))
				THEN
					UPDATE utilisateur
					SET solde = solde - NEW.montant * (1 + (SELECT montant FROM taxe WHERE id=NEW.taxe_id))
					WHERE id = NEW.emetteur_id;
				ELSE
					SET NEW.montant = 0, NEW.description = "cancelled transaction";
			END CASE;
		WHEN NEW.vers_compte = false 
        THEN
			UPDATE utilisateur
			SET solde = solde + NEW.montant * (1 - (SELECT montant FROM taxe WHERE id=NEW.taxe_id))
			WHERE id = NEW.emetteur_id;
	END CASE;
ELSEIF (((SELECT solde FROM utilisateur WHERE id = NEW.emetteur_id) >= NEW.montant * (1 + (SELECT montant FROM taxe WHERE id=NEW.taxe_id))) AND NEW.destinataire_id IS NOT NULL)
THEN
	UPDATE utilisateur
	SET solde = solde - NEW.montant * (1 + (SELECT montant FROM taxe WHERE id=NEW.taxe_id))
	WHERE id = NEW.emetteur_id;
	UPDATE utilisateur
	SET solde = solde + NEW.montant
	WHERE id = NEW.destinataire_id;
ELSE
    SET NEW.montant = 0, NEW.description = "cancelled transaction";
END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
  `compte_bancaire` varchar(34) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

LOCK TABLES `utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` VALUES (1,'pers1@mail.com','1234',216.50,'ribpers1'),(2,'pers2@mail.com','5678',140.00,'ribpers2'),(3,'pers3@mail.com','password',100.00,'ribpers3');
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur_utilisateur`
--

DROP TABLE IF EXISTS `utilisateur_utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur_utilisateur` (
  `utilisateur_1_id` int NOT NULL,
  `utilisateur_2_id` int NOT NULL,
  PRIMARY KEY (`utilisateur_1_id`,`utilisateur_2_id`),
  KEY `utilisateur_2_id` (`utilisateur_2_id`),
  CONSTRAINT `utilisateur_utilisateur_ibfk_1` FOREIGN KEY (`utilisateur_1_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `utilisateur_utilisateur_ibfk_2` FOREIGN KEY (`utilisateur_2_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur_utilisateur`
--

LOCK TABLES `utilisateur_utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur_utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur_utilisateur` VALUES (2,1),(3,1),(1,2),(1,3);
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

-- Dump completed on 2023-07-11 16:16:42
