-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 31, 2020 at 11:13 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `epidemiological_investigations`
--

-- --------------------------------------------------------

--
-- Table structure for table `comuni`
--

CREATE TABLE `comuni` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(10,6) NOT NULL,
  `istat` varchar(10) NOT NULL,
  `data_istituzione` date NOT NULL,
  `territorio` int(1) NOT NULL,
  `mare` tinyint(1) NOT NULL,
  `id_provincia` int(10) NOT NULL,
  `id_responsabile` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `contagi`
--

CREATE TABLE `contagi` (
  `id` int(10) NOT NULL,
  `malattia` int(2) NOT NULL,
  `persone_ricoverate` int(10) NOT NULL,
  `persone_in_cura` int(10) NOT NULL,
  `id_segnalazione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `decessi`
--

CREATE TABLE `decessi` (
  `id` int(10) NOT NULL,
  `causa` int(2) NOT NULL,
  `numero` int(10) NOT NULL,
  `id_segnalazione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `provincie`
--

CREATE TABLE `provincie` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(10,6) NOT NULL,
  `capoluogo` varchar(30) NOT NULL,
  `id_regione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `regioni`
--

CREATE TABLE `regioni` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(10,6) NOT NULL,
  `capoluogo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `segnalazioni_contagi`
--

CREATE TABLE `segnalazioni_contagi` (
  `id` int(10) NOT NULL,
  `data` date NOT NULL,
  `id_comune` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `segnalazioni_decessi`
--

CREATE TABLE `segnalazioni_decessi` (
  `id` int(10) NOT NULL,
  `anno` int(4) NOT NULL,
  `id_provincia` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `utenti`
--

CREATE TABLE `utenti` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `ruolo` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `utenti`
--

INSERT INTO `utenti` (`id`, `nome`, `cognome`, `username`, `password`, `ruolo`) VALUES
(1, 'admin', 'admin', 'admin', '-7396891a4abefbea4216f742b211ea204e9856378c03b44757e090d54bb756e8', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comuni`
--
ALTER TABLE `comuni`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_provincia` (`id_provincia`),
  ADD KEY `id_responsabile` (`id_responsabile`);

--
-- Indexes for table `contagi`
--
ALTER TABLE `contagi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_segnalazione` (`id_segnalazione`);

--
-- Indexes for table `decessi`
--
ALTER TABLE `decessi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_segnalazione` (`id_segnalazione`);

--
-- Indexes for table `provincie`
--
ALTER TABLE `provincie`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`),
  ADD KEY `id_regione` (`id_regione`);

--
-- Indexes for table `regioni`
--
ALTER TABLE `regioni`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`);

--
-- Indexes for table `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_comune` (`id_comune`);

--
-- Indexes for table `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_provincia` (`id_provincia`);

--
-- Indexes for table `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comuni`
--
ALTER TABLE `comuni`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `contagi`
--
ALTER TABLE `contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `decessi`
--
ALTER TABLE `decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `provincie`
--
ALTER TABLE `provincie`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `regioni`
--
ALTER TABLE `regioni`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `utenti`
--
ALTER TABLE `utenti`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comuni`
--
ALTER TABLE `comuni`
  ADD CONSTRAINT `id_provincia` FOREIGN KEY (`id_provincia`) REFERENCES `provincie` (`id`),
  ADD CONSTRAINT `id_responsabile` FOREIGN KEY (`id_responsabile`) REFERENCES `utenti` (`id`);

--
-- Constraints for table `contagi`
--
ALTER TABLE `contagi`
  ADD CONSTRAINT `id_segnalazione` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_contagi` (`id`);

--
-- Constraints for table `decessi`
--
ALTER TABLE `decessi`
  ADD CONSTRAINT `decessi_ibfk_1` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_decessi` (`id`);

--
-- Constraints for table `provincie`
--
ALTER TABLE `provincie`
  ADD CONSTRAINT `provincie_ibfk_1` FOREIGN KEY (`id_regione`) REFERENCES `regioni` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  ADD CONSTRAINT `id_comune` FOREIGN KEY (`id_comune`) REFERENCES `comuni` (`id`);

--
-- Constraints for table `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  ADD CONSTRAINT `segnalazioni_decessi_ibfk_1` FOREIGN KEY (`id_provincia`) REFERENCES `provincie` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
