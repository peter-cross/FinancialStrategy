-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 18, 2018 at 01:26 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `strategy`
--

-- --------------------------------------------------------

--
-- Table structure for table `chofaccs`
--

CREATE TABLE `chofaccs` (
  `CHOFACCSID` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `CRCY_CRCYID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `chofaccs`
--

INSERT INTO `chofaccs` (`CHOFACCSID`, `CODE`, `NAME`, `CRCY_CRCYID`) VALUES
(6151, 'NFX', 'Knsfshnfq (NFX)', 6051),
(6201, 'FXUJ', 'Knsfshnfq (FXUJ)', 6051),
(6301, 'RLR', 'Rfsfljrjsy', 6052);

-- --------------------------------------------------------

--
-- Table structure for table `crcy`
--

CREATE TABLE `crcy` (
  `CRCYID` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `crcy`
--

INSERT INTO `crcy` (`CRCYID`, `CODE`, `NAME`) VALUES
(6051, 'HFI', 'Hfsfinfs Itqqfw'),
(6052, 'ZXI', 'Z.X. Itqqfw'),
(6053, 'JZW', 'Jzwt'),
(6351, 'OUD', 'Ofufsjxj Djs');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `employeeId` bigint(20) NOT NULL,
  `iD` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`employeeId`, `iD`, `name`, `phone`, `address`) VALUES
(13301, '56', 'Ujyjw Hwtxx', '159-704-3998', '6657 - 6883 Xjdrtzw Xy, Afshtzajw, GH A1G 5P6'),
(13451, '57', 'Fqnhj Ptwdt', '159-731-9358', '');

-- --------------------------------------------------------

--
-- Table structure for table `expense`
--

CREATE TABLE `expense` (
  `expenseId` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `expense`
--

INSERT INTO `expense` (`expenseId`, `code`, `name`, `category`) VALUES
(12751, '556', 'Rfns Uwtizhynts Jcujsxjx', 'Uwtizhynts'),
(12801, '557', 'Tajwmjfi Uwtizhynts Jcujsxjx', 'Uwtizhynts'),
(13401, '558', 'Qfgtzw Uwtizhynts Jcujsxjx', 'Uwtizhynts');

-- --------------------------------------------------------

--
-- Table structure for table `gl`
--

CREATE TABLE `gl` (
  `GLID` bigint(20) NOT NULL,
  `ACCTGRP` varchar(255) DEFAULT NULL,
  `CONTRAACCT` int(11) DEFAULT NULL,
  `FRGNCRCY` int(11) DEFAULT NULL,
  `GLNUMBER` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `CHOFACCS_CHOFACCSID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gl`
--

INSERT INTO `gl` (`GLID`, `ACCTGRP`, `CONTRAACCT`, `FRGNCRCY`, `GLNUMBER`, `NAME`, `QUANTITY`, `TYPE`, `CHOFACCS_CHOFACCSID`) VALUES
(6451, 'Hfxm fsi Hfxm Jvznafqjsyx', 0, 1, '6555', 'Hfxm Ts Mfsi', 0, 'Gfqfshj Xmjjy', 6151),
(6501, 'Xmtwy-Yjwr Nsajxyrjsyx', 0, 1, '6505', 'Rfwpjyfgqj Xjhzwnynjx', 1, 'Gfqfshj Xmjjy', 6151),
(6551, 'Hfxm fsi Hfxm Jvznafqjsyx', 0, 1, '655', 'Hfxm ts Mfsi', 0, 'Gfqfshj Xmjjy', 6201),
(6552, 'Xmtwy-Yjwr Nsajxyrjsyx', 0, 1, '650', 'Rfwpjyfgqj Xjhzwnynjx', 1, 'Gfqfshj Xmjjy', 6201),
(6701, 'Hzxytrjwx Wjhjnafgqj', 0, 0, '6655', 'Fhhtzsyx Wjhjnafgqj', 0, 'Gfqfshj Xmjjy', 6151),
(6703, 'Hzxytrjwx Wjhjnafgqj', 0, 0, '665', 'Fhhtzsyx Wjhjnafgqj', 0, 'Gfqfshj Xmjjy', 6201),
(6751, 'Rjwhmfsinej', 0, 0, '6755', 'Nsajsytwd ktw Xfqj', 1, 'Gfqfshj Xmjjy', 6151),
(6754, 'Rjwhmfsinej', 0, 0, '675', 'Nsajsytwd ktw Xfqj', 1, 'Gfqfshj Xmjjy', 6201),
(6801, 'Uwjufni Jcujsxjx', 0, 0, '6855', 'Uwjufni Jcujsxjx', 0, 'Gfqfshj Xmjjy', 6151),
(6803, 'Uwjufni Jcujsxjx', 0, 0, '685', 'Uwjufni Jcujsxjx', 0, 'Gfqfshj Xmjjy', 6201),
(8701, 'Kncji Fxxjyx', 0, 0, '7555', 'Kncji Fxxjyx', 1, 'Gfqfshj Xmjjy', 6151),
(8703, 'Kncji Fxxjyx', 0, 0, '755', 'Kncji Fxxjyx', 1, 'Gfqfshj Xmjjy', 6201),
(8705, 'Sts-Yfslngqj Fxxjyx', 0, 0, '7655', 'Nsyfslngqj Fxxjyx', 1, 'Gfqfshj Xmjjy', 6151),
(8707, 'Sts-Yfslngqj Fxxjyx', 0, 0, '765', 'Nsyfslngqj Fxxjyx', 1, 'Gfqfshj Xmjjy', 6201),
(8709, 'Kncji Fxxjyx Ijuwjhnfynts', 1, 0, '7355', 'Frtwynefynts tk Kncji Fxxjyx', 0, 'Gfqfshj Xmjjy', 6151),
(8711, 'Kncji Fxxjyx Ijuwjhnfynts', 1, 0, '735', 'Frtwynefynts tk Kncji Fxxjyx', 0, 'Gfqfshj Xmjjy', 6201),
(8713, 'Sts-Yfslngqj Fxxjyx Frtwynefynts', 1, 0, '7455', 'Frtwynefynts tk Nsyfslngqj Fxxjyx', 0, 'Gfqfshj Xmjjy', 6151),
(8715, 'Sts-Yfslngqj Fxxjyx Frtwynefynts', 1, 0, '745', 'Frtwynefynts tk Nsyfslngqj Fxxjyx', 0, 'Gfqfshj Xmjjy', 6201);

-- --------------------------------------------------------

--
-- Table structure for table `glanalytics`
--

CREATE TABLE `glanalytics` (
  `GLANALYTICSID` bigint(20) NOT NULL,
  `ANALYTICSCONTROL` varchar(255) DEFAULT NULL,
  `ANALYTICSTYPE` varchar(255) DEFAULT NULL,
  `LINENUM` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `glanalytics`
--

INSERT INTO `glanalytics` (`GLANALYTICSID`, `ANALYTICSCONTROL`, `ANALYTICSTYPE`, `LINENUM`) VALUES
(8501, 'Gfsp Fhhtzsyx', 'Gfqfshj', 0),
(8502, 'Gzxnsjxx Ufwysjwx', 'Ujwnti', 1),
(8503, 'Gfsp Fhhtzsyx', 'Gfqfshj', 0),
(8504, 'Gzxnsjxx Ufwysjwx', 'Ujwnti', 1),
(8551, 'Rfwpjyfgqj Xjhzwnynjx', 'Gfqfshj', 0),
(8552, 'Gzxnsjxx Ufwysjwx', 'Ujwnti', 1),
(8553, 'Rfwpjyfgqj Xjhzwnynjx', 'Gfqfshj', 0),
(8554, 'Gzxnsjxx Ufwysjwx', 'Ujwnti', 1),
(8601, 'Gzxnsjxx Ufwysjwx', 'Gfqfshj', 0),
(8602, 'Gzxnsjxx Ufwysjwx', 'Gfqfshj', 0),
(8603, 'Nsajsytwd', 'Gfqfshj', 0),
(8604, 'Qthfyntsx', 'Gfqfshj', 1),
(8605, 'Nsajsytwd', 'Gfqfshj', 0),
(8606, 'Qthfyntsx', 'Gfqfshj', 1),
(8651, 'Jcujsxjx', 'Gfqfshj', 0),
(8652, 'Jcujsxjx', 'Gfqfshj', 0),
(8702, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8704, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8706, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8708, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8710, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8712, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8714, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0),
(8716, 'Qtsl Yjwr Fxxjyx', 'Gfqfshj', 0);

-- --------------------------------------------------------

--
-- Table structure for table `gl_glanalytics`
--

CREATE TABLE `gl_glanalytics` (
  `GL_GLID` bigint(20) NOT NULL,
  `analytics_GLANALYTICSID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gl_glanalytics`
--

INSERT INTO `gl_glanalytics` (`GL_GLID`, `analytics_GLANALYTICSID`) VALUES
(6451, 8501),
(6451, 8502),
(6501, 8551),
(6501, 8552),
(6551, 8503),
(6551, 8504),
(6552, 8553),
(6552, 8554),
(6701, 8601),
(6703, 8602),
(6751, 8603),
(6751, 8604),
(6754, 8605),
(6754, 8606),
(6801, 8651),
(6803, 8652),
(8701, 8702),
(8703, 8704),
(8705, 8706),
(8707, 8708),
(8709, 8710),
(8711, 8712),
(8713, 8714),
(8715, 8716);

-- --------------------------------------------------------

--
-- Table structure for table `hashmap`
--

CREATE TABLE `hashmap` (
  `hashmapId` bigint(20) NOT NULL,
  `hashkey` varchar(255) DEFAULT NULL,
  `hashvalue` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hashmap`
--

INSERT INTO `hashmap` (`hashmapId`, `hashkey`, `hashvalue`) VALUES
(11201, 'CA', 'Hzwwjsy Fxxjyx'),
(11202, 'LTA', 'Qtsl-Yjwr Fxxjyx'),
(11203, 'CL', 'Hzwwjsy Qnfgnqnynjx'),
(11204, 'LTL', 'Qtsl-Yjwr Qnfgnqnynjx'),
(11205, 'SHE', 'Xmfwjmtqijw\'x Jvznyd'),
(11206, 'CCE', 'Hfxm fsi Hfxm Jvznafqjsyx'),
(11207, 'STI', 'Xmtwy-Yjwr Nsajxyrjsyx'),
(11208, 'AR', 'Fhhtzsyx Wjhjnafgqj'),
(11209, 'INV', 'Nsajsytwd'),
(11210, 'PE', 'Uwjufni Jcujsxjx'),
(11211, 'CR', 'Hzxytrjwx Wjhjnafgqj'),
(11212, 'TXR', 'Yfcjx Wjhjnafgqj'),
(11213, 'NR', 'Styjx Wjhjnafgqj'),
(11214, 'DR', 'Inanijsix Wjhjnafgqj'),
(11215, 'MRZ', 'Rjwhmfsinej'),
(11216, 'DM', 'Inwjhy Rfyjwnfqx'),
(11217, 'WIP', 'Btwp-Ns-Uwtlwjxx'),
(11218, 'FG', 'Knsnxmji Lttix'),
(11219, 'FA', 'Kncji Fxxjyx'),
(11220, 'NTA', 'Sts-Yfslngqj Fxxjyx'),
(11221, 'LTI', 'Qtsl-Yjwr Nsajxyrjsyx'),
(11222, 'DPR', 'Ijuwjhnfynts'),
(11223, 'FAD', 'Kncji Fxxjyx Ijuwjhnfynts'),
(11224, 'NTAD', 'Sts-Yfslngqj Fxxjyx Frtwynefynts'),
(11225, 'SVP', 'Xzuuqnjwx fsi Ajsitwx Ufdfgqj'),
(11226, 'CCP', 'Hwjiny Hfwix Ufdfgqj'),
(11227, 'SR', 'Xfqfwnjx Ufdfgqj'),
(11228, 'PP', 'Ujsxnts Ufdfgqj'),
(11229, 'TXP', 'Yfcjx Ufdfgqj'),
(11230, 'AOE', 'Fhhwzji Tujwfynsl Jcujsxjx'),
(11231, 'IP', 'Nsyjwjxy Ufdfgqj'),
(11232, 'CPLTD', 'Hzwwjsy utwynts tk Qtsl-Yjwr Ijgy'),
(11233, 'NP', 'Styjx Ufdfgqj'),
(11234, 'BP', 'Gtsix Ufdfgqj'),
(11235, 'FPITX', 'Knsjx, Ujsfqynjx, Nsyjwjxy ts Yfcjx'),
(11236, 'DIVP', 'Inanijsix Ufdfgqj'),
(11237, 'INTX', 'Nshtrj Yfcjx'),
(11238, 'SLTX', 'Xfqjx Yfcjx'),
(11239, 'PRTX', 'Ufdwtqq Yfcjx'),
(11240, 'OTTX', 'Tymjw Yfcjx'),
(11241, 'PSDIV', 'Uwjkjwwji Xmfwjx Inanijsix'),
(11242, 'CSDIV', 'Htrrts Xmfwjx Inanijsix'),
(11243, 'CRL', 'Hwjiny Qtfsx'),
(11244, 'WL', 'Bfwwfsyd Qnfgnqnynjx'),
(11245, 'CST', 'Htrrts Xythp'),
(11246, 'RE', 'Wjyfnsji Jfwsnslx'),
(11247, 'SLR', 'Xfqjx Wjajszj'),
(11248, 'INTI', 'Nsyjwjxy Nshtrj'),
(11249, 'INVI', 'Nsajxyrjsy Nshtrj'),
(11250, 'FEG', 'Ktwjnls Jchmfslj Lfnsx'),
(11251, 'COGS', 'Htxy Tk Lttix Xtqi'),
(11252, 'OPE', 'Tujwfynsl Jcujsxjx'),
(11253, 'DPR', 'Ijuwjhnfynts'),
(11254, 'FEXL', 'Ktwjnls Jchmfslj Qtxxjx'),
(11255, 'OPRI', 'Tujwfynsl Nshtrj'),
(11256, 'INVL', 'Nsajxyrjsy Qtxxjx'),
(11257, 'IBIT', 'Nshtrj Gjktwj Nsyjwjxy Fsi Yfcjx'),
(11258, 'INTE', 'Nsyjwjxy Jcujsxj'),
(11259, 'INCTX', 'Nshtrj Yfcjx'),
(11260, 'NI', 'Sjy Nshtrj'),
(11261, 'CRSL', 'Hwjiny Xfqjx'),
(11262, 'CSSL', 'Hfxm Xfqjx'),
(11263, 'RD', 'W & I'),
(11264, 'DSN', 'Ijxnls'),
(11265, 'PCH', 'Uzwhmfxnsl'),
(11266, 'PRD', 'Uwtizhynts'),
(11267, 'MKT', 'Rfwpjynsl'),
(11268, 'DST', 'Inxywngzynts'),
(11269, 'CSS', 'Hzxytrjw Xjwanhj'),
(11270, 'ADM', 'Firnsnxywfynts'),
(11271, 'BalSht', 'Gfqfshj Xmjjy'),
(11272, 'IncStt', 'Nshtrj Xyfyjrjsy'),
(11273, 'ChOfAccs', 'Hmfwy Tk Fhhtzsyx'),
(11274, 'Crcy', 'Hzwwjshd'),
(11275, 'GlAcc', 'L/Q Fhhy'),
(11276, 'GlAcct', 'L/Q Fhhtzsy'),
(11277, 'GlNumber', 'L/Q Szrgjw'),
(11278, 'AcctName', 'Fhhtzsy Sfrj'),
(11279, 'AccType', 'Yduj tk Fhhtzsy'),
(11280, 'AccGrp', 'Fhhtzsy Lwtzu'),
(11281, 'FrgnCrcy', 'Ktwjnls Hzwwjshd'),
(11282, 'ContraAcct', 'Htsywf Fhhtzsy'),
(11283, 'LglEntity', 'Qjlfq Jsynyd'),
(11284, 'LglName', 'Qjlfq Sfrj'),
(11285, 'TractsDscr', 'Ywfsxfhynts Ijxhwnuynts'),
(11286, 'TransModel', 'Ywfsxfhyntsx Rtijq'),
(11287, 'A1str', 'Gfsp Fhhtzsyx'),
(11288, 'A1', 'GfspFhhtzsy'),
(11289, 'A2str', 'Gzxnsjxx Ufwysjwx'),
(11290, 'A2', 'GzxnsjxxUfwysjw'),
(11291, 'A3str', 'Htsywfhyx'),
(11292, 'A3', 'Htsywfhy'),
(11293, 'A4str', 'Jruqtdjjx'),
(11294, 'A4', 'Jruqtdjj'),
(11295, 'A5str', 'Jcujsxjx'),
(11296, 'A5', 'Jcujsxj'),
(11297, 'A6str', 'Nsajsytwd'),
(11298, 'A6', 'Nsajsytwd'),
(11299, 'A7str', 'Qjlfq Jsynynjx'),
(11300, 'A7', 'QlqJsynyd'),
(11301, 'A8str', 'Qthfyntsx'),
(11302, 'A8', 'Qthfynts'),
(11303, 'A9str', 'Qtsl Yjwr Fxxjyx'),
(11304, 'A9', 'QtslYjwrFxxjy'),
(11305, 'A10str', 'Rfwpjyfgqj Xjhzwnynjx'),
(11306, 'A10', 'Xjhzwnynjx'),
(11307, 'A11str', 'Uwtizhyx'),
(11308, 'A11', 'Uwtizhy'),
(11309, 'A12str', 'Xmfwjmtqijwx'),
(11310, 'A12', 'Xmfwjmtqijw'),
(11311, 'A13str', 'Bfwwfsynjx'),
(11312, 'A13', 'Bfwwfsyd'),
(11313, 'A14str', 'Gzxnsjxx Qnsjx'),
(11314, 'A14', 'GzxnsjxxQnsj'),
(13201, 'exp', 'Jcujsxj Nyjr');

-- --------------------------------------------------------

--
-- Table structure for table `lglentity`
--

CREATE TABLE `lglentity` (
  `LGLENTITYID` bigint(20) NOT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `CONTACT` varchar(255) DEFAULT NULL,
  `ID` varchar(255) DEFAULT NULL,
  `LGLNAME` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PHONE` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lglentity`
--

INSERT INTO `lglentity` (`LGLENTITYID`, `ADDRESS`, `CONTACT`, `ID`, `LGLNAME`, `NAME`, `PHONE`) VALUES
(2601, '6883 Xjdrtzw Xywjjy, Afshtzajw, GH A1G 8U8', 'Ujyjw Hwtxx', '1', 'Ifsijqtns Jsyjwuwnxj Qyi.', 'Ifsijqnts', '6-159-333-0220'),
(2602, '656 Iwfpj Xywjjy, Afshtzajw, GH', 'Wfd Jfs', '2', 'Xufwyfs Lwtzu Nsh.', 'Xufwyfs', '6-159-222-5656'),
(7351, '', '', '3', 'Ejxy Nsh', 'Ejxy', '');

-- --------------------------------------------------------

--
-- Table structure for table `lglentitycharts`
--

CREATE TABLE `lglentitycharts` (
  `LGLENTITYCHARTSID` bigint(20) NOT NULL,
  `CHARTNAME` varchar(255) DEFAULT NULL,
  `LINENUM` int(11) DEFAULT NULL,
  `CHOFACCS_CHOFACCSID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lglentitycharts`
--

INSERT INTO `lglentitycharts` (`LGLENTITYCHARTSID`, `CHARTNAME`, `LINENUM`, `CHOFACCS_CHOFACCSID`) VALUES
(8301, 'Knsfshnfq', 0, 6151),
(8302, 'Rfsfljrjsy', 1, 6301),
(8303, 'Knsfshnfq', 0, 6201),
(8304, 'Rfsfljrjsy', 1, 6301),
(8406, 'Rfsfljrjsy', 2, 6301),
(8404, 'Knsfshnfq 6', 0, 6151),
(8405, 'Knsfshnfq 7', 1, 6201),
(13502, 'Rfsfljrjsy', 1, 6301),
(13501, 'Knsfshnfq', 0, 6151),
(13653, 'Knsfshnfq', 0, 6151),
(13654, 'Rfsfljrjsy', 1, 6301);

-- --------------------------------------------------------

--
-- Table structure for table `lglentity_lglentitycharts`
--

CREATE TABLE `lglentity_lglentitycharts` (
  `LglEntity_LGLENTITYID` bigint(20) NOT NULL,
  `lglEntityCharts_LGLENTITYCHARTSID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lglentity_lglentitycharts`
--

INSERT INTO `lglentity_lglentitycharts` (`LglEntity_LGLENTITYID`, `lglEntityCharts_LGLENTITYCHARTSID`) VALUES
(2602, 8303),
(2602, 8304),
(7351, 8406),
(7351, 8404),
(7351, 8405),
(2601, 13653),
(2601, 13654);

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE `location` (
  `locationID` bigint(20) NOT NULL,
  `id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `location`
--

INSERT INTO `location` (`locationID`, `id`, `name`, `address`, `phone`, `contact`) VALUES
(12601, '1', 'Mtrj tkknhj', '6883 Xjdrtzw Xywjjy, Afshtzajw, GH A1G 8U8', '159-704-3998', 'Ujyjw Hwtxx'),
(12651, '2', 'Qfslfwf Htqqjlj', '655 Bjxy 94 Faj., Afshtzajw', '159-878-0066', '');

-- --------------------------------------------------------

--
-- Table structure for table `sequence`
--

CREATE TABLE `sequence` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sequence`
--

INSERT INTO `sequence` (`SEQ_NAME`, `SEQ_COUNT`) VALUES
('SEQ_GEN', '13700');

-- --------------------------------------------------------

--
-- Table structure for table `tacct`
--

CREATE TABLE `tacct` (
  `TACCTID` bigint(20) NOT NULL,
  `COL` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ROW` int(11) DEFAULT NULL,
  `CHOFACCS_CHOFACCSID` bigint(20) DEFAULT NULL,
  `GL_GLID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tacct`
--

INSERT INTO `tacct` (`TACCTID`, `COL`, `NAME`, `ROW`, `CHOFACCS_CHOFACCSID`, `GL_GLID`) VALUES
(3955, 0, 'F/U', 1, 0, NULL),
(3956, 5, 'Rfyjwnfqx', 3, 0, NULL),
(8851, 0, 'F/U', 1, 6151, NULL),
(8852, 4, 'Nsajsytwd', 1, 6151, NULL),
(8853, 4, 'Rfyjwnfqx', 4, 6151, NULL),
(8854, 4, 'Nsajsytwd', 1, 6201, NULL),
(8855, 4, 'Rfyjwnfqx', 4, 6201, NULL),
(8856, 8, 'Uwtizhynts', 4, 6201, NULL),
(8857, 12, 'Uwtizhyx', 4, 6201, NULL),
(8858, 16, 'Xfqjx', 1, 6201, NULL),
(9102, 13, 'Uwtizhyx', 3, 6151, NULL),
(9202, 26, 'Hfxm', 0, 6151, NULL),
(9203, 23, 'Hmjvzjx', 3, 6151, NULL),
(9301, 0, 'F/U', 0, 6151, NULL),
(9302, 9, 'Uwtizhynts', 3, 6151, NULL),
(9303, 6, 'Jcujsxjx', 6, 6151, NULL),
(9304, 20, 'F/W', 0, 6151, NULL),
(9305, 17, 'Xfqjx', 0, 6151, NULL),
(9651, 0, 'Ufdwtqq Yfcjx', 13, 6151, NULL),
(9652, 3, 'Ufdwtqq', 13, 6151, NULL),
(9751, 0, 'Hfxm', 1, 6301, NULL),
(9752, 4, 'F/U', 1, 6301, NULL),
(9753, 4, 'Ufdwtqq', 4, 6301, NULL),
(9901, 12, 'Uwtkny', 9, 6151, NULL),
(9951, 12, 'LXY', 11, 6151, NULL),
(9952, 12, 'UXY', 13, 6151, NULL),
(10001, 4, 'Nsajsytwd', 0, 6151, NULL),
(10002, 4, 'Rfyjwnfqx', 3, 6151, NULL),
(10051, 4, 'LXY', 7, 6301, NULL),
(10151, 23, 'Hwjiny Hfwix', 6, 6151, NULL),
(10251, 4, 'UXY', 10, 6301, NULL),
(12151, 9, 'F/U', 1, 6151, NULL),
(12159, 14, 'Nsajsytwd', 1, 6151, NULL),
(12351, 14, 'Rfyjwnfq', 4, 6151, NULL),
(12401, 0, 'Xythpx', 1, 6151, 6501),
(12402, 5, 'Nsajsytwd', 1, 6151, 6751),
(12451, 0, 'Fhhtzsyx Wjhjnafgqj', 4, 6151, 6701),
(12452, 5, 'Rfwpjyfgqj Xjhzwnynjx', 4, 6151, 6501),
(12501, 14, 'Uwtizhynts Jcujsxjx', 7, 6151, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tacct_corrcx`
--

CREATE TABLE `tacct_corrcx` (
  `TAcct_TACCTID` bigint(20) DEFAULT NULL,
  `VALUE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tacct_corrcx`
--

INSERT INTO `tacct_corrcx` (`TAcct_TACCTID`, `VALUE`) VALUES
(3956, 3),
(8852, 1),
(8858, 1),
(8858, 4),
(8853, 4),
(8857, 4),
(8856, 4),
(9202, 3),
(9102, 3),
(9202, 0),
(9203, 3),
(9304, 0),
(9302, 3),
(9302, 6),
(9305, 0),
(9305, 3),
(9305, 7),
(9303, 6),
(9305, 9),
(9304, 11),
(9303, 13),
(9303, 14),
(9652, 13),
(9752, 1),
(9753, 4),
(9304, 13),
(9901, 9),
(9951, 11),
(10002, 3),
(10001, 0),
(10051, 7),
(9202, 6),
(10151, 6),
(10251, 10),
(12159, 1),
(12351, 4),
(12402, 1),
(12452, 4),
(12501, 7);

-- --------------------------------------------------------

--
-- Table structure for table `tacct_corrdx`
--

CREATE TABLE `tacct_corrdx` (
  `TAcct_TACCTID` bigint(20) DEFAULT NULL,
  `VALUE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tacct_corrdx`
--

INSERT INTO `tacct_corrdx` (`TAcct_TACCTID`, `VALUE`) VALUES
(3955, 3),
(8855, 4),
(8854, 1),
(8857, 4),
(8856, 4),
(8851, 1),
(8851, 4),
(9203, 3),
(9102, 3),
(9304, 0),
(9304, 3),
(9302, 3),
(9301, 0),
(9301, 3),
(9301, 6),
(9305, 0),
(9303, 6),
(9303, 7),
(9303, 9),
(9301, 11),
(9652, 13),
(9651, 13),
(9651, 14),
(9751, 1),
(9751, 4),
(9901, 9),
(9951, 11),
(9952, 13),
(10002, 3),
(10001, 0),
(9751, 7),
(9304, 6),
(10151, 6),
(9751, 10),
(12151, 1),
(12151, 4),
(12401, 1),
(12451, 4),
(12151, 7);

-- --------------------------------------------------------

--
-- Table structure for table `tractn`
--

CREATE TABLE `tractn` (
  `TRACTNID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ROW` int(11) DEFAULT NULL,
  `CX_TACCTID` bigint(20) DEFAULT NULL,
  `DX_TACCTID` bigint(20) DEFAULT NULL,
  `CHOFACCS_CHOFACCSID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractn`
--

INSERT INTO `tractn` (`TRACTNID`, `DESCRIPTION`, `ROW`, `CX_TACCTID`, `DX_TACCTID`, `CHOFACCS_CHOFACCSID`) VALUES
(3957, 'Rfyjwnfqx uzwhmfxj', 3, 3955, 3956, NULL),
(8859, 'Nsajsytwd uzwhmfxj', 1, 8851, 8852, 6151),
(8860, 'Rffyjwnfqx uzwhmfxj', 4, 8851, 8853, 6151),
(8861, 'Rfyjwnfqx ktw uwtizhynts', 4, 8855, 8856, 6201),
(8862, 'Uwtizhynts tzyuzy', 4, 8856, 8857, 6201),
(8863, 'Htxy tk nsajsytwd xtqi', 1, 8854, 8858, 6201),
(8864, 'Htxy tk uwtizhyx xtqi', 4, 8857, 8858, 6201),
(9210, 'Hqjfwji hmjvzjx', 3, 9203, 9202, 6151),
(9311, 'Uwtizhynts tzyuzy', 3, 9302, 9102, 6151),
(9312, 'Uwtizhynts jcujsxjx', 6, 9303, 9302, 6151),
(9313, 'Xjwanhjx wjhjnaji', 6, 9301, 9303, 6151),
(9315, 'Htxy tk uwtizhyx xtqi', 3, 9102, 9305, 6151),
(9316, 'Tajwmjfi jcujsxjx', 7, 9303, 9305, 6151),
(9317, 'Xfqjx sjy tk yfcjx', 0, 9305, 9304, 6151),
(9321, 'Ufni gd hfxm', 0, 9304, 9202, 6151),
(9322, 'Ufni gd hmjvzj', 3, 9304, 9203, 6151),
(9653, 'Nshtrj yfc', 13, 9651, 9652, 6151),
(9654, 'Ufdwtqq jcujsxjx', 13, 9652, 9303, 6151),
(9655, 'Htrufsd ufdwtqq yfcjx', 14, 9651, 9303, 6151),
(9757, 'Nsatnhjx ufni', 1, 9751, 9752, 6301),
(9758, 'Xfqfwnjx ufni', 4, 9751, 9753, 6301),
(9902, 'Sts-tujwfyntsfq jcujsxjx', 9, 9303, 9901, 6151),
(9903, 'Uwtkny kwtr tujwfyntsx', 9, 9901, 9305, 6151),
(9953, 'LXY ts xfqjx', 11, 9951, 9304, 6151),
(9954, 'UXY ts xfqjx', 13, 9952, 9304, 6151),
(9955, 'LXY ts uzwhmfxjx', 11, 9301, 9951, 6151),
(10003, 'Nsajsytwd uzwhmfxj', 0, 9301, 10001, 6151),
(10004, 'Rfyjwnfqx uzwhmfxj', 3, 9301, 10002, 6151),
(10005, 'Htxy tk nsajsytwd xtqi', 0, 10001, 9305, 6151),
(10006, 'Rfyjwnfqx yt uwtizhynts', 3, 10002, 9302, 6151),
(10053, 'LXY ufni', 7, 9751, 10051, 6301),
(10152, 'Ufni gd hwjiny hfwi', 6, 9304, 10151, 6151),
(10153, 'Hwjiny ufdrjsyx', 6, 10151, 9202, 6151),
(10252, 'UXY ufni', 10, 9751, 10251, 6301),
(12161, 'Nsajsytwd uzwhmfxj', 1, 12151, 12159, 6151),
(12352, 'Rfyjwnfqx uzwhmfxj', 4, 12151, 12351, 6151),
(12403, 'Uwtizhynts jcujsxjx', 1, 12401, 12402, 6151),
(12453, 'Sts-tujwfyntsfq jcujsxjx', 4, 12451, 12452, 6151),
(12502, 'Htsywfhyji Xjwanhjx ', 7, 12151, 12501, 6151);

-- --------------------------------------------------------

--
-- Table structure for table `tractnsdscr`
--

CREATE TABLE `tractnsdscr` (
  `tractnsDscrId` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractnsdscr`
--

INSERT INTO `tractnsdscr` (`tractnsDscrId`, `code`, `description`) VALUES
(10601, '56', 'Nsajsytwd Uzwhmfxj'),
(10602, '57', 'Rfyjwnfqx Uzwhmfxj'),
(10603, '58', 'Xjwanhjx wjhjnaji'),
(10651, '59', 'Htxy tk nsajsytwd xtqi'),
(10652, '50', 'Rfyjwnfqx yt uwtizhynts'),
(10653, '51', 'Uwtizhynts tzyuzy'),
(10654, '52', 'Htxy tk uwtizhyx xtqi'),
(10655, '53', 'Uwtizhynts jcujsxjx'),
(10656, '54', 'Tajwmjfi jcujsxjx'),
(10657, '65', 'Sts-tujwfyntsfq jcujsxjx'),
(10658, '66', 'Ufdwtqq jcujsxjx'),
(10659, '67', 'Nshtrj yfc'),
(10660, '68', 'Htrufsd ufdwtqq yfcjx'),
(10661, '69', 'LXY ts uzwhmfxjx'),
(10662, '60', 'LXY ts xfqjx'),
(10663, '61', 'UXY ts xfqjx'),
(10664, '62', 'Uwtkny kwtr tujwfyntsx'),
(10665, '63', 'Xfqjx sjy tk yfcjx'),
(10666, '64', 'Ufni gd hfxm'),
(10667, '75', 'Ufni gd hmjvzj'),
(10668, '76', 'Ufni gd hwjiny hfwi'),
(10669, '76', 'Hqjfwji hmjvzjx'),
(10670, '77', 'Hwjiny ufdrjsyx');

-- --------------------------------------------------------

--
-- Table structure for table `tractnsmodel`
--

CREATE TABLE `tractnsmodel` (
  `TRACTNSMODELID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `LGLENTITY_LGLENTITYID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractnsmodel`
--

INSERT INTO `tractnsmodel` (`TRACTNSMODELID`, `NAME`, `LGLENTITY_LGLENTITYID`) VALUES
(1108, 'Ljsjwfq Jsyjwuwnxj Rtijq', 2601),
(2204, 'Nsajxyrjsy Rtijq', 2601),
(3954, 'Xufwyfs Rtijq', 2602),
(8865, 'Ejxy Jsyjwuwnxj', 7351);

-- --------------------------------------------------------

--
-- Table structure for table `tractnsmodel_tacct`
--

CREATE TABLE `tractnsmodel_tacct` (
  `TractnsModel_TRACTNSMODELID` bigint(20) NOT NULL,
  `taccts_TACCTID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractnsmodel_tacct`
--

INSERT INTO `tractnsmodel_tacct` (`TractnsModel_TRACTNSMODELID`, `taccts_TACCTID`) VALUES
(1108, 9102),
(1108, 9202),
(1108, 9203),
(1108, 9301),
(1108, 9302),
(1108, 9303),
(1108, 9304),
(1108, 9305),
(1108, 9651),
(1108, 9652),
(1108, 9751),
(1108, 9752),
(1108, 9753),
(1108, 9901),
(1108, 9951),
(1108, 9952),
(1108, 10001),
(1108, 10002),
(1108, 10051),
(1108, 10151),
(1108, 10251),
(2204, 12151),
(2204, 12159),
(2204, 12351),
(2204, 12401),
(2204, 12402),
(2204, 12451),
(2204, 12452),
(2204, 12501),
(8865, 8851),
(8865, 8852),
(8865, 8853),
(8865, 8854),
(8865, 8855),
(8865, 8856),
(8865, 8857),
(8865, 8858);

-- --------------------------------------------------------

--
-- Table structure for table `tractnsmodel_tractn`
--

CREATE TABLE `tractnsmodel_tractn` (
  `TractnsModel_TRACTNSMODELID` bigint(20) NOT NULL,
  `tractns_TRACTNID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractnsmodel_tractn`
--

INSERT INTO `tractnsmodel_tractn` (`TractnsModel_TRACTNSMODELID`, `tractns_TRACTNID`) VALUES
(1108, 9210),
(1108, 9311),
(1108, 9312),
(1108, 9313),
(1108, 9315),
(1108, 9316),
(1108, 9317),
(1108, 9321),
(1108, 9322),
(1108, 9653),
(1108, 9654),
(1108, 9655),
(1108, 9757),
(1108, 9758),
(1108, 9902),
(1108, 9903),
(1108, 9953),
(1108, 9954),
(1108, 9955),
(1108, 10003),
(1108, 10004),
(1108, 10005),
(1108, 10006),
(1108, 10053),
(1108, 10152),
(1108, 10153),
(1108, 10252),
(2204, 12161),
(2204, 12352),
(2204, 12403),
(2204, 12453),
(2204, 12502),
(8865, 8859),
(8865, 8860),
(8865, 8861),
(8865, 8862),
(8865, 8863),
(8865, 8864);

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE `unit` (
  `unitId` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `unitType` varchar(255) DEFAULT NULL,
  `baseUnit` varchar(255) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `decimals` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`unitId`, `code`, `name`, `unitType`, `baseUnit`, `rate`, `decimals`) VALUES
(12851, 'nyjr', 'Nyjr', 'Nyjrx', '', 0, 0),
(12901, 'l', 'Lwfr', 'Bjnlmy', '', 0, 0),
(12951, 'Q', 'Qnyywjx', 'Atqzrj', '', 0, 0),
(13001, 'rl', 'Rnqqnlwfr', 'Bjnlmy', 'l', 0.001, 0),
(13051, 'te', 'Tzshj', 'Bjnlmy', 'l', 28.35, 0),
(13101, 'qg', 'Utzsi', 'Bjnlmy', 'l', 453.59, 0),
(13102, 'pl', 'Pnqtlwfr', 'Bjnlmy', 'l', 1000, 0),
(13151, 'rQ', 'Rnqqnqnywjx', 'Atqzrj', 'Q', 0.001, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chofaccs`
--
ALTER TABLE `chofaccs`
  ADD PRIMARY KEY (`CHOFACCSID`),
  ADD KEY `FK_CHOFACCS_CRCY_CRCYID` (`CRCY_CRCYID`);

--
-- Indexes for table `crcy`
--
ALTER TABLE `crcy`
  ADD PRIMARY KEY (`CRCYID`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`employeeId`);

--
-- Indexes for table `expense`
--
ALTER TABLE `expense`
  ADD PRIMARY KEY (`expenseId`);

--
-- Indexes for table `gl`
--
ALTER TABLE `gl`
  ADD PRIMARY KEY (`GLID`),
  ADD KEY `FK_GL_CHOFACCS_CHOFACCSID` (`CHOFACCS_CHOFACCSID`);

--
-- Indexes for table `glanalytics`
--
ALTER TABLE `glanalytics`
  ADD PRIMARY KEY (`GLANALYTICSID`);

--
-- Indexes for table `gl_glanalytics`
--
ALTER TABLE `gl_glanalytics`
  ADD PRIMARY KEY (`GL_GLID`,`analytics_GLANALYTICSID`),
  ADD KEY `GLCCNTGLANALYTICSnlytcsGLANALYTICSID` (`analytics_GLANALYTICSID`);

--
-- Indexes for table `hashmap`
--
ALTER TABLE `hashmap`
  ADD PRIMARY KEY (`hashmapId`);

--
-- Indexes for table `lglentity`
--
ALTER TABLE `lglentity`
  ADD PRIMARY KEY (`LGLENTITYID`);

--
-- Indexes for table `lglentitycharts`
--
ALTER TABLE `lglentitycharts`
  ADD KEY `FK_LGLENTITYCHARTS_CHOFACCS_CHOFACCSID` (`CHOFACCS_CHOFACCSID`);

--
-- Indexes for table `lglentity_lglentitycharts`
--
ALTER TABLE `lglentity_lglentitycharts`
  ADD KEY `LGLENTITYLGLENTITYCHARTSLglEntityLGLENTITYID` (`LglEntity_LGLENTITYID`);

--
-- Indexes for table `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`locationID`);

--
-- Indexes for table `sequence`
--
ALTER TABLE `sequence`
  ADD PRIMARY KEY (`SEQ_NAME`);

--
-- Indexes for table `tacct`
--
ALTER TABLE `tacct`
  ADD PRIMARY KEY (`TACCTID`),
  ADD KEY `FK_TACCT_GL_GLID` (`GL_GLID`);

--
-- Indexes for table `tacct_corrcx`
--
ALTER TABLE `tacct_corrcx`
  ADD KEY `FK_TAcct_CORRCX_TAcct_TACCTID` (`TAcct_TACCTID`);

--
-- Indexes for table `tacct_corrdx`
--
ALTER TABLE `tacct_corrdx`
  ADD KEY `FK_TAcct_CORRDX_TAcct_TACCTID` (`TAcct_TACCTID`);

--
-- Indexes for table `tractn`
--
ALTER TABLE `tractn`
  ADD PRIMARY KEY (`TRACTNID`),
  ADD KEY `FK_TRACTN_CX_TACCTID` (`CX_TACCTID`),
  ADD KEY `FK_TRACTN_DX_TACCTID` (`DX_TACCTID`);

--
-- Indexes for table `tractnsmodel`
--
ALTER TABLE `tractnsmodel`
  ADD PRIMARY KEY (`TRACTNSMODELID`),
  ADD KEY `FK_TRACTNSMODEL_LGLENTITY_LGLENTITYID` (`LGLENTITY_LGLENTITYID`);

--
-- Indexes for table `tractnsmodel_tacct`
--
ALTER TABLE `tractnsmodel_tacct`
  ADD PRIMARY KEY (`TractnsModel_TRACTNSMODELID`,`taccts_TACCTID`),
  ADD KEY `FK_TRACTNSMODEL_TACCT_taccts_TACCTID` (`taccts_TACCTID`);

--
-- Indexes for table `tractnsmodel_tractn`
--
ALTER TABLE `tractnsmodel_tractn`
  ADD PRIMARY KEY (`TractnsModel_TRACTNSMODELID`,`tractns_TRACTNID`),
  ADD KEY `TRNSCTNSMODELTRACTNtractnsTRACTNID` (`tractns_TRACTNID`);

--
-- Indexes for table `unit`
--
ALTER TABLE `unit`
  ADD PRIMARY KEY (`unitId`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chofaccs`
--
ALTER TABLE `chofaccs`
  ADD CONSTRAINT `FK_CHOFACCS_CRCY_CRCYID` FOREIGN KEY (`CRCY_CRCYID`) REFERENCES `crcy` (`CRCYID`);

--
-- Constraints for table `gl`
--
ALTER TABLE `gl`
  ADD CONSTRAINT `FK_GL_CHOFACCS_CHOFACCSID` FOREIGN KEY (`CHOFACCS_CHOFACCSID`) REFERENCES `chofaccs` (`CHOFACCSID`);

--
-- Constraints for table `gl_glanalytics`
--
ALTER TABLE `gl_glanalytics`
  ADD CONSTRAINT `FK_GL_GLANALYTICS_GL_GLID` FOREIGN KEY (`GL_GLID`) REFERENCES `gl` (`GLID`),
  ADD CONSTRAINT `FK_GL_GLANALYTICS_analytics_GLANALYTICSID` FOREIGN KEY (`analytics_GLANALYTICSID`) REFERENCES `glanalytics` (`GLANALYTICSID`),
  ADD CONSTRAINT `GLCCNTGLANALYTICSnlytcsGLANALYTICSID` FOREIGN KEY (`analytics_GLANALYTICSID`) REFERENCES `glanalytics` (`GLANALYTICSID`),
  ADD CONSTRAINT `GL_GLANALYTICS_GL_GLID` FOREIGN KEY (`GL_GLID`) REFERENCES `gl` (`GLID`);

--
-- Constraints for table `lglentitycharts`
--
ALTER TABLE `lglentitycharts`
  ADD CONSTRAINT `FK_LGLENTITYCHARTS_CHOFACCS_CHOFACCSID` FOREIGN KEY (`CHOFACCS_CHOFACCSID`) REFERENCES `chofaccs` (`CHOFACCSID`);

--
-- Constraints for table `lglentity_lglentitycharts`
--
ALTER TABLE `lglentity_lglentitycharts`
  ADD CONSTRAINT `LGLENTITYLGLENTITYCHARTSLglEntityLGLENTITYID` FOREIGN KEY (`LglEntity_LGLENTITYID`) REFERENCES `lglentity` (`LGLENTITYID`);

--
-- Constraints for table `tacct`
--
ALTER TABLE `tacct`
  ADD CONSTRAINT `FK_TACCT_GL_GLID` FOREIGN KEY (`GL_GLID`) REFERENCES `gl` (`GLID`);

--
-- Constraints for table `tacct_corrcx`
--
ALTER TABLE `tacct_corrcx`
  ADD CONSTRAINT `FK_TAcct_CORRCX_TAcct_TACCTID` FOREIGN KEY (`TAcct_TACCTID`) REFERENCES `tacct` (`TACCTID`);

--
-- Constraints for table `tacct_corrdx`
--
ALTER TABLE `tacct_corrdx`
  ADD CONSTRAINT `FK_TAcct_CORRDX_TAcct_TACCTID` FOREIGN KEY (`TAcct_TACCTID`) REFERENCES `tacct` (`TACCTID`);

--
-- Constraints for table `tractn`
--
ALTER TABLE `tractn`
  ADD CONSTRAINT `FK_TRACTN_CX_TACCTID` FOREIGN KEY (`CX_TACCTID`) REFERENCES `tacct` (`TACCTID`),
  ADD CONSTRAINT `FK_TRACTN_DX_TACCTID` FOREIGN KEY (`DX_TACCTID`) REFERENCES `tacct` (`TACCTID`);

--
-- Constraints for table `tractnsmodel`
--
ALTER TABLE `tractnsmodel`
  ADD CONSTRAINT `FK_TRACTNSMODEL_LGLENTITY_LGLENTITYID` FOREIGN KEY (`LGLENTITY_LGLENTITYID`) REFERENCES `lglentity` (`LGLENTITYID`);

--
-- Constraints for table `tractnsmodel_tacct`
--
ALTER TABLE `tractnsmodel_tacct`
  ADD CONSTRAINT `FK_TRACTNSMODEL_TACCT_taccts_TACCTID` FOREIGN KEY (`taccts_TACCTID`) REFERENCES `tacct` (`TACCTID`),
  ADD CONSTRAINT `TRNSCTNSMDLTACCTTrnsctnsMdelTRACTNSMODELID` FOREIGN KEY (`TractnsModel_TRACTNSMODELID`) REFERENCES `tractnsmodel` (`TRACTNSMODELID`);

--
-- Constraints for table `tractnsmodel_tractn`
--
ALTER TABLE `tractnsmodel_tractn`
  ADD CONSTRAINT `FK_TRACTNSMODEL_TRACTN_TractnsModel_TRACTNSMODELID` FOREIGN KEY (`TractnsModel_TRACTNSMODELID`) REFERENCES `tractnsmodel` (`TRACTNSMODELID`),
  ADD CONSTRAINT `FK_TRACTNSMODEL_TRACTN_tractns_TRACTNID` FOREIGN KEY (`tractns_TRACTNID`) REFERENCES `tractn` (`TRACTNID`),
  ADD CONSTRAINT `TRNSCTNSMDLTRNSACTNTrnsctnsMdlTRNSACTIONSMODELID` FOREIGN KEY (`TractnsModel_TRACTNSMODELID`) REFERENCES `tractnsmodel` (`TRACTNSMODELID`),
  ADD CONSTRAINT `TRNSCTNSMODELTRACTNtrnsactnsTRACTNID` FOREIGN KEY (`tractns_TRACTNID`) REFERENCES `tractn` (`TRACTNID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
