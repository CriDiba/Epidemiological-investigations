-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 15, 2020 alle 11:36
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
  `superficie` decimal(12,2) NOT NULL,
  `istat` varchar(10) NOT NULL,
  `data_istituzione` date NOT NULL,
  `territorio` int(1) NOT NULL,
  `mare` tinyint(1) NOT NULL,
  `id_provincia` int(10) NOT NULL,
  `id_responsabile` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `comuni`
--

INSERT INTO `comuni` (`id`, `nome`, `superficie`, `istat`, `data_istituzione`, `territorio`, `mare`, `id_provincia`, `id_responsabile`) VALUES
(2, 'Legnago', '79.67', '023044', '1900-01-01', 0, 0, 1, 3),
(3, 'Bovolone', '41.41', '023012', '1900-01-01', 0, 0, 1, 3),
(4, 'Cerea', '70.41', '023025', '1900-01-01', 0, 0, 1, 3),
(5, 'Milano', '181.67', '015146', '1900-01-01', 0, 0, 6, 3),
(6, 'Brescia', '90.34', '017029', '1900-01-01', 0, 0, 7, 3);

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

--
-- Dump dei dati per la tabella `contagi`
--

INSERT INTO `contagi` (`id`, `malattia`, `persone_ricoverate`, `persone_in_cura`, `id_segnalazione`) VALUES
(9, 0, 10, 10, 2),
(10, 1, 60, 30, 2),
(11, 4, 20, 70, 2),
(12, 3, 50, 50, 2),
(13, 2, 12, 50, 2),
(14, 5, 30, 30, 2),
(15, 6, 20, 50, 2),
(16, 7, 70, 0, 2),
(17, 0, 5, 50, 3),
(18, 1, 30, 20, 3),
(19, 4, 70, 50, 3),
(20, 3, 10, 60, 3),
(21, 2, 60, 10, 3),
(22, 5, 60, 70, 3),
(23, 6, 10, 90, 3),
(24, 7, 0, 20, 3),
(25, 0, 90, 10, 4),
(26, 1, 5, 20, 4),
(27, 4, 0, 10, 4),
(28, 3, 30, 0, 4),
(29, 2, 10, 30, 4),
(30, 5, 30, 30, 4),
(31, 6, 10, 50, 4),
(32, 7, 3, 5, 4),
(33, 0, 48, 23, 5),
(34, 1, 56, 15, 5),
(35, 4, 0, 2, 5),
(36, 3, 59, 0, 5),
(37, 2, 20, 0, 5),
(38, 5, 12, 15, 5),
(39, 6, 30, 23, 5),
(40, 7, 20, 10, 5),
(41, 0, 14, 4, 6),
(42, 1, 7, 3, 6),
(43, 4, 5, 5, 6),
(44, 3, 8, 8, 6),
(45, 2, 27, 5, 6),
(46, 5, 12, 2, 6),
(47, 6, 5, 4, 6),
(48, 7, 12, 6, 6),
(49, 0, 2, 2, 7),
(50, 1, 6, 2, 7),
(51, 4, 2, 5, 7),
(52, 3, 12, 4, 7),
(53, 2, 5, 6, 7),
(54, 5, 4, 4, 7),
(55, 6, 5, 0, 7),
(56, 7, 3, 4, 7),
(57, 0, 5, 1, 8),
(58, 1, 2, 0, 8),
(59, 4, 1, 4, 8),
(60, 3, 5, 9, 8),
(61, 2, 2, 5, 8),
(62, 5, 5, 1, 8),
(63, 6, 2, 2, 8),
(64, 7, 1, 4, 8),
(65, 0, 5, 5, 9),
(66, 1, 2, 5, 9),
(67, 4, 4, 5, 9),
(68, 3, 2, 85, 9),
(69, 2, 7, 1, 9),
(70, 5, 6, 23, 9),
(71, 6, 5, 6, 9),
(72, 7, 2, 4, 9),
(73, 0, 75, 55, 10),
(74, 1, 52, 62, 10),
(75, 4, 68, 78, 10),
(76, 3, 25, 55, 10),
(77, 2, 34, 11, 10),
(78, 5, 5, 23, 10),
(79, 6, 45, 0, 10),
(80, 7, 55, 5, 10),
(81, 0, 20, 35, 11),
(82, 1, 23, 53, 11),
(83, 4, 58, 85, 11),
(84, 3, 85, 42, 11),
(85, 2, 68, 2, 11),
(86, 5, 54, 25, 11),
(87, 6, 42, 42, 11),
(88, 7, 58, 58, 11),
(89, 0, 12, 5, 12),
(90, 1, 5, 2, 12),
(91, 4, 6, 1, 12),
(92, 3, 31, 3, 12),
(93, 2, 5, 4, 12),
(94, 5, 8, 5, 12),
(95, 6, 5, 7, 12),
(96, 7, 7, 3, 12),
(97, 0, 57, 0, 13),
(98, 1, 6, 5, 13),
(99, 4, 8, 2, 13),
(100, 3, 6, 7, 13),
(101, 2, 4, 0, 13),
(102, 5, 5, 3, 13),
(103, 6, 4, 5, 13),
(104, 7, 6, 0, 13),
(105, 0, 52, 8, 14),
(106, 1, 5, 7, 14),
(107, 4, 4, 5, 14),
(108, 3, 2, 4, 14),
(109, 2, 7, 5, 14),
(110, 5, 7, 5, 14),
(111, 6, 5, 5, 14),
(112, 7, 5, 8, 14),
(113, 0, 67, 42, 15),
(114, 1, 45, 23, 15),
(115, 4, 45, 24, 15),
(116, 3, 35, 0, 15),
(117, 2, 21, 45, 15),
(118, 5, 56, 52, 15),
(119, 6, 75, 75, 15),
(120, 7, 42, 24, 15),
(121, 0, 53, 35, 16),
(122, 1, 42, 23, 16),
(123, 4, 24, 72, 16),
(124, 3, 54, 10, 16),
(125, 2, 35, 24, 16),
(126, 5, 42, 0, 16),
(127, 6, 83, 42, 16),
(128, 7, 42, 53, 16),
(129, 0, 5, 0, 17),
(130, 1, 2, 4, 17),
(131, 4, 3, 6, 17),
(132, 3, 6, 9, 17),
(133, 2, 6, 0, 17),
(134, 5, 3, 9, 17),
(135, 6, 5, 8, 17),
(136, 7, 1, 5, 17),
(137, 0, 2, 7, 18),
(138, 1, 5, 8, 18),
(139, 4, 5, 4, 18),
(140, 3, 5, 8, 18),
(141, 2, 8, 5, 18),
(142, 5, 7, 5, 18),
(143, 6, 5, 8, 18),
(144, 7, 7, 7, 18),
(145, 0, 1, 2, 19),
(146, 1, 4, 1, 19),
(147, 4, 2, 5, 19),
(148, 3, 1, 2, 19),
(149, 2, 1, 5, 19),
(150, 5, 1, 1, 19),
(151, 6, 5, 2, 19),
(152, 7, 5, 5, 19),
(153, 0, 25, 12, 20),
(154, 1, 52, 25, 20),
(155, 4, 3, 25, 20),
(156, 3, 0, 1, 20),
(157, 2, 1, 5, 20),
(158, 5, 45, 1, 20),
(159, 6, 5, 21, 20),
(160, 7, 4, 4, 20),
(161, 0, 25, 53, 21),
(162, 1, 21, 12, 21),
(163, 4, 57, 21, 21),
(164, 3, 12, 52, 21),
(165, 2, 52, 12, 21),
(166, 5, 42, 52, 21),
(167, 6, 12, 37, 21),
(168, 7, 7, 8, 21),
(169, 0, 2, 4, 22),
(170, 1, 4, 25, 22),
(171, 4, 2, 1, 22),
(172, 3, 1, 0, 22),
(173, 2, 12, 25, 22),
(174, 5, 2, 5, 22),
(175, 6, 4, 1, 22),
(176, 7, 5, 2, 22),
(177, 0, 5, 8, 23),
(178, 1, 4, 5, 23),
(179, 4, 8, 4, 23),
(180, 3, 4, 5, 23),
(181, 2, 5, 6, 23),
(182, 5, 4, 5, 23),
(183, 6, 8, 5, 23),
(184, 7, 5, 8, 23),
(185, 0, 21, 24, 24),
(186, 1, 45, 3, 24),
(187, 4, 8, 9, 24),
(188, 3, 45, 85, 24),
(189, 2, 5, 6, 24),
(190, 5, 4, 44, 24),
(191, 6, 5, 85, 24),
(192, 7, 4, 54, 24),
(193, 0, 115, 52, 25),
(194, 1, 54, 45, 25),
(195, 4, 85, 5, 25),
(196, 3, 5, 8, 25),
(197, 2, 87, 5, 25),
(198, 5, 8, 21, 25),
(199, 6, 58, 8, 25),
(200, 7, 4, 6, 25),
(201, 0, 5, 3, 26),
(202, 1, 4, 5, 26),
(203, 4, 7, 5, 26),
(204, 3, 2, 5, 26),
(205, 2, 7, 5, 26),
(206, 5, 5, 12, 26),
(207, 6, 5, 4, 26),
(208, 7, 8, 5, 26),
(209, 0, 12, 4, 27),
(210, 1, 0, 2, 27),
(211, 4, 4, 8, 27),
(212, 3, 6, 6, 27),
(213, 2, 5, 8, 27),
(214, 5, 4, 5, 27),
(215, 6, 4, 2, 27),
(216, 7, 8, 7, 27),
(217, 0, 32, 42, 28),
(218, 1, 5, 55, 28),
(219, 4, 24, 40, 28),
(220, 3, 0, 25, 28),
(221, 2, 12, 25, 28),
(222, 5, 8, 5, 28),
(223, 6, 25, 5, 28),
(224, 7, 4, 63, 28),
(225, 0, 99, 54, 29),
(226, 1, 22, 45, 29),
(227, 4, 15, 4, 29),
(228, 3, 52, 35, 29),
(229, 2, 35, 35, 29),
(230, 5, 45, 5, 29),
(231, 6, 52, 14, 29),
(232, 7, 14, 52, 29),
(233, 0, 2, 5, 30),
(234, 1, 9, 6, 30),
(235, 4, 6, 6, 30),
(236, 3, 5, 8, 30),
(237, 2, 6, 3, 30),
(238, 5, 8, 6, 30),
(239, 6, 5, 6, 30),
(240, 7, 5, 9, 30);

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

--
-- Dump dei dati per la tabella `decessi`
--

INSERT INTO `decessi` (`id`, `causa`, `numero`, `id_segnalazione`) VALUES
(13, 0, 53, 4),
(14, 1, 5, 4),
(15, 2, 25, 4),
(16, 3, 12, 4),
(17, 0, 55, 5),
(18, 1, 52, 5),
(19, 2, 42, 5),
(20, 3, 86, 5),
(21, 0, 76, 6),
(22, 1, 24, 6),
(23, 2, 52, 6),
(24, 3, 54, 6),
(25, 0, 73, 7),
(26, 1, 85, 7),
(27, 2, 45, 7),
(28, 3, 86, 7),
(29, 0, 73, 8),
(30, 1, 19, 8),
(31, 2, 73, 8),
(32, 3, 91, 8),
(33, 0, 42, 9),
(34, 1, 52, 9),
(35, 2, 75, 9),
(36, 3, 46, 9),
(37, 0, 73, 10),
(38, 1, 85, 10),
(39, 2, 45, 10),
(40, 3, 96, 10),
(41, 0, 86, 11),
(42, 1, 72, 11),
(43, 2, 48, 11),
(44, 3, 96, 11),
(45, 0, 73, 12),
(46, 1, 85, 12),
(47, 2, 46, 12),
(48, 3, 82, 12),
(49, 0, 82, 13),
(50, 1, 76, 13),
(51, 2, 82, 13),
(52, 3, 37, 13),
(53, 0, 73, 14),
(54, 1, 82, 14),
(55, 2, 76, 14),
(56, 3, 86, 14),
(57, 0, 73, 15),
(58, 1, 85, 15),
(59, 2, 75, 15),
(60, 3, 68, 15),
(61, 0, 120, 16),
(62, 1, 153, 16),
(63, 2, 42, 16),
(64, 3, 24, 16),
(65, 0, 73, 17),
(66, 1, 82, 17),
(67, 2, 46, 17),
(68, 3, 29, 17),
(69, 0, 51, 18),
(70, 1, 23, 18),
(71, 2, 56, 18),
(72, 3, 45, 18),
(73, 0, 46, 19),
(74, 1, 54, 19),
(75, 2, 56, 19),
(76, 3, 31, 19),
(77, 0, 15, 20),
(78, 1, 26, 20),
(79, 2, 18, 20),
(80, 3, 80, 20),
(81, 0, 85, 21),
(82, 1, 75, 21),
(83, 2, 52, 21),
(84, 3, 75, 21),
(85, 0, 75, 22),
(86, 1, 85, 22),
(87, 2, 27, 22),
(88, 3, 26, 22),
(89, 0, 43, 23),
(90, 1, 82, 23),
(91, 2, 54, 23),
(92, 3, 57, 23),
(93, 0, 85, 24),
(94, 1, 77, 24),
(95, 2, 68, 24),
(96, 3, 75, 24);

-- --------------------------------------------------------

--
-- Struttura della tabella `province`
--

CREATE TABLE `province` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(12,2) NOT NULL,
  `capoluogo` varchar(30) NOT NULL,
  `id_regione` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `province`
--

INSERT INTO `province` (`id`, `nome`, `superficie`, `capoluogo`, `id_regione`) VALUES
(1, 'Verona', '3096.39', 'Verona', 1),
(2, 'Belluno', '3610.20', 'Belluno', 1),
(3, 'Vicenza', '2722.53', 'Vicenza', 1),
(4, 'Padova', '2144.15', 'Padova', 1),
(5, 'Rovigo', '1819.35', 'Rovigo', 1),
(6, 'Milano', '1575.00', 'Milano', 2),
(7, 'Brescia', '4786.00', 'Brescia', 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `regioni`
--

CREATE TABLE `regioni` (
  `id` int(10) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `superficie` decimal(12,2) NOT NULL,
  `capoluogo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `regioni`
--

INSERT INTO `regioni` (`id`, `nome`, `superficie`, `capoluogo`) VALUES
(1, 'Veneto', '18345.00', 'Venezia'),
(2, 'Lombardia', '23863.65', 'Milano');

-- --------------------------------------------------------

--
-- Struttura della tabella `segnalazioni_contagi`
--

CREATE TABLE `segnalazioni_contagi` (
  `id` int(10) NOT NULL,
  `data` date NOT NULL,
  `id_comune` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `segnalazioni_contagi`
--

INSERT INTO `segnalazioni_contagi` (`id`, `data`, `id_comune`) VALUES
(2, '2017-07-14', 2),
(3, '2017-07-14', 3),
(4, '2017-07-14', 4),
(5, '2017-07-14', 5),
(6, '2017-07-14', 6),
(7, '2019-07-14', 2),
(8, '2019-07-14', 3),
(9, '2019-07-14', 4),
(10, '2019-07-14', 5),
(11, '2019-07-14', 6),
(12, '2020-03-14', 2),
(13, '2020-03-14', 3),
(14, '2020-03-14', 4),
(15, '2020-03-14', 5),
(16, '2020-03-14', 6),
(17, '2020-06-30', 2),
(18, '2020-06-30', 3),
(19, '2020-06-30', 4),
(20, '2020-06-30', 5),
(21, '2020-06-30', 6),
(22, '2020-07-07', 3),
(23, '2020-07-07', 4),
(24, '2020-07-07', 5),
(25, '2020-07-07', 6),
(26, '2020-07-14', 2),
(27, '2020-07-14', 3),
(28, '2020-07-14', 5),
(29, '2020-07-14', 6),
(30, '2020-07-14', 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `segnalazioni_decessi`
--

CREATE TABLE `segnalazioni_decessi` (
  `id` int(10) NOT NULL,
  `data` date NOT NULL,
  `id_provincia` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `segnalazioni_decessi`
--

INSERT INTO `segnalazioni_decessi` (`id`, `data`, `id_provincia`) VALUES
(4, '2017-07-14', 1),
(5, '2017-07-14', 2),
(6, '2017-07-14', 3),
(7, '2017-07-14', 4),
(8, '2017-07-14', 5),
(9, '2018-07-14', 6),
(10, '2018-07-14', 7),
(11, '2019-07-14', 1),
(12, '2019-07-14', 2),
(13, '2019-07-14', 3),
(14, '2019-07-14', 4),
(15, '2019-07-14', 5),
(16, '2019-07-14', 6),
(17, '2019-07-14', 7),
(18, '2020-07-14', 1),
(19, '2020-07-14', 2),
(20, '2020-07-14', 3),
(21, '2020-07-14', 4),
(22, '2020-07-14', 5),
(23, '2020-07-14', 6),
(24, '2020-07-14', 7);

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
(0, 'admin', 'admin', 'admin', '-7396891a4abefbea4216f742b211ea204e9856378c03b44757e090d54bb756e8', 0),
(1, 'Mario', 'Rossi', 'a', '-35687eed35e44235053dce4c65dc23b258791007eb83b18d467f887a5011b745', 2),
(2, 'Luigi', 'Bianchi', 'b', '3e23e8160039594a33894f6564e1b1348bbd7a0088d42c4acb73eeaed59c009d', 1),
(3, 'Luca', 'Verdi', 'c', '2e7d2c03a9507ae265ecf5b5356885a53393a2029d241394997265a1a25aefc6', 3),
(4, 'Dante', 'Alighieri', 'd', '18ac3e7343f016890c510e93f935261169d9e3f565436429830faf0934f4f8e4', 3),
(5, 'Nicola', 'Fausto Spoto', 'fspoto', '-50094a05c26bbcfb0e69c4093dc22a6fe00d765b931aba79614084fa753297b2', 0);

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
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT per la tabella `contagi`
--
ALTER TABLE `contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=241;

--
-- AUTO_INCREMENT per la tabella `decessi`
--
ALTER TABLE `decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;

--
-- AUTO_INCREMENT per la tabella `province`
--
ALTER TABLE `province`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT per la tabella `regioni`
--
ALTER TABLE `regioni`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT per la tabella `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT per la tabella `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT per la tabella `utenti`
--
ALTER TABLE `utenti`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
  ADD CONSTRAINT `id_segnalazione` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_contagi` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `decessi`
--
ALTER TABLE `decessi`
  ADD CONSTRAINT `decessi_ibfk_1` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni_decessi` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `province`
--
ALTER TABLE `province`
  ADD CONSTRAINT `province_ibfk_1` FOREIGN KEY (`id_regione`) REFERENCES `regioni` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `segnalazioni_contagi`
--
ALTER TABLE `segnalazioni_contagi`
  ADD CONSTRAINT `id_comune` FOREIGN KEY (`id_comune`) REFERENCES `comuni` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `segnalazioni_decessi`
--
ALTER TABLE `segnalazioni_decessi`
  ADD CONSTRAINT `segnalazioni_decessi_ibfk_1` FOREIGN KEY (`id_provincia`) REFERENCES `province` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
