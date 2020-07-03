-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 03, 2020 alle 10:08
-- Versione del server: 10.4.13-MariaDB
-- Versione PHP: 7.4.7

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
-- Struttura della tabella `comuni`
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
-- Struttura della tabella `contagi`
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
-- Struttura della tabella `decessi`
--

CREATE TABLE `decessi` (
  `id` int(10) NOT NULL,
  `causa` int(2) NOT NULL,
  `numero` int(10) NOT NULL,
  `id_segnalazione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `province`
--

CREATE TABLE `province` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(10,6) NOT NULL,
  `capoluogo` varchar(30) NOT NULL,
  `id_regione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `regioni`
--

CREATE TABLE `regioni` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(10,6) NOT NULL,
  `capoluogo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `segnalazioni_contagi`
--

CREATE TABLE `segnalazioni_contagi` (
  `id` int(10) NOT NULL,
  `data` date NOT NULL,
  `id_comune` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `segnalazioni_decessi`
--

CREATE TABLE `segnalazioni_decessi` (
  `id` int(10) NOT NULL,
  `anno` int(4) NOT NULL,
  `id_provincia` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
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
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`id`, `nome`, `cognome`, `username`, `password`, `ruolo`) VALUES
(1, 'admin', 'admin', 'admin', '-7396891a4abefbea4216f742b211ea204e9856378c03b44757e090d54bb756e8', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `comuni`
--
ALTER TABLE `comuni`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_provincia` (`id_provincia`),
  ADD KEY `id_responsabile` (`id_responsabile`);

--
-- Indici per le tabelle `contagi`
--
ALTER TABLE `contagi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_segnalazione` (`id_segnalazione`);

--
-- Indici per le tabelle `decessi`
--
ALTER TABLE `decessi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_segnalazione` (`id_segnalazione`);

--
-- Indici per le tabelle `province`
--
ALTER TABLE `province`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`),
  ADD KEY `id_regione` (`id_regione`);

--
-- Indici per le tabelle `regioni`
--
ALTER TABLE `regioni`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`);

--
-- Indici per le tabelle `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_comune` (`id_comune`);

--
-- Indici per le tabelle `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_provincia` (`id_provincia`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `comuni`
--
ALTER TABLE `comuni`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `contagi`
--
ALTER TABLE `contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `decessi`
--
ALTER TABLE `decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `province`
--
ALTER TABLE `province`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `regioni`
--
ALTER TABLE `regioni`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `utenti`
--
ALTER TABLE `utenti`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `comuni`
--
ALTER TABLE `comuni`
  ADD CONSTRAINT `id_provincia` FOREIGN KEY (`id_provincia`) REFERENCES `province` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `id_responsabile` FOREIGN KEY (`id_responsabile`) REFERENCES `utenti` (`id`) ON DELETE SET NULL;

--
-- Limiti per la tabella `contagi`
--
ALTER TABLE `contagi`
  ADD CONSTRAINT `id_segnalazione` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_contagi` (`id`);

--
-- Limiti per la tabella `decessi`
--
ALTER TABLE `decessi`
  ADD CONSTRAINT `decessi_ibfk_1` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_decessi` (`id`);

--
-- Limiti per la tabella `province`
--
ALTER TABLE `province`
  ADD CONSTRAINT `province_ibfk_1` FOREIGN KEY (`id_regione`) REFERENCES `regioni` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  ADD CONSTRAINT `id_comune` FOREIGN KEY (`id_comune`) REFERENCES `comuni` (`id`);

--
-- Limiti per la tabella `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  ADD CONSTRAINT `segnalazioni_decessi_ibfk_1` FOREIGN KEY (`id_provincia`) REFERENCES `province` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
