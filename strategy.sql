-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 16, 2017 at 07:05 PM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `strategy`
--

-- --------------------------------------------------------

--
-- Table structure for table `coa`
--

CREATE TABLE IF NOT EXISTS `coa` (
  `COAID` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `CURRENCY_CURRENCYID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `coa`
--

INSERT INTO `coa` (`COAID`, `CODE`, `NAME`, `CURRENCY_CURRENCYID`) VALUES
(6151, 'NFX', 'Knsfshnfq (NFX)', 6053),
(6201, 'FXUJ', 'Knsfshnfq (FXUJ)', 6051),
(6301, 'RLR', 'Rfsfljrjsy', 6052);

-- --------------------------------------------------------

--
-- Table structure for table `currency`
--

CREATE TABLE IF NOT EXISTS `currency` (
  `CURRENCYID` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `currency`
--

INSERT INTO `currency` (`CURRENCYID`, `CODE`, `NAME`) VALUES
(6051, 'HFI', 'Hfsfinfs itqqfw'),
(6052, 'ZXI', 'Z.X. itqqfw'),
(6053, 'JZW', 'Jzwt'),
(6351, 'OUD', 'Ofufsjxj Djs');

-- --------------------------------------------------------

--
-- Table structure for table `gl`
--

CREATE TABLE IF NOT EXISTS `gl` (
  `GLID` bigint(20) NOT NULL,
  `ACCOUNTGROUP` varchar(255) DEFAULT NULL,
  `CONTRAACCOUNT` int(11) DEFAULT NULL,
  `FOREIGNCURRENCY` int(11) DEFAULT NULL,
  `GLNUMBER` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `COA_COAID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gl`
--

INSERT INTO `gl` (`GLID`, `ACCOUNTGROUP`, `CONTRAACCOUNT`, `FOREIGNCURRENCY`, `GLNUMBER`, `NAME`, `QUANTITY`, `TYPE`, `COA_COAID`) VALUES
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

CREATE TABLE IF NOT EXISTS `glanalytics` (
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

CREATE TABLE IF NOT EXISTS `gl_glanalytics` (
  `GL_GLID` bigint(20) NOT NULL,
  `analytics_GLANALYTICSID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gl_glanalytics`
--

INSERT INTO `gl_glanalytics` (`GL_GLID`, `analytics_GLANALYTICSID`) VALUES
(6451, 8501),
(6451, 8502),
(6551, 8503),
(6551, 8504),
(6501, 8551),
(6501, 8552),
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
-- Table structure for table `legalentity`
--

CREATE TABLE IF NOT EXISTS `legalentity` (
  `LEGALENTITYID` bigint(20) NOT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `CONTACT` varchar(255) DEFAULT NULL,
  `ID` varchar(255) DEFAULT NULL,
  `LEGALNAME` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PHONE` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `legalentity`
--

INSERT INTO `legalentity` (`LEGALENTITYID`, `ADDRESS`, `CONTACT`, `ID`, `LEGALNAME`, `NAME`, `PHONE`) VALUES
(2601, '6883 Xjdrtzw Xywjjy, Afshtzajw, GH', 'Ujyjw Hwtxx', '1', 'Ifsijqtns Jsyjwuwnxj Nsh.', 'Ifsijqnts', '6-159-333-0220'),
(2602, '656 Iwfpj Xywjjy, Afshtzajw, GH', 'Wfd Jfs', '2', 'Xufwyfs Lwtzu Nsh.', 'Xufwyfs', '6-159-222-5656'),
(7351, '', '', '3', 'Ejxy Nsh', 'Ejxy', '');

-- --------------------------------------------------------

--
-- Table structure for table `legalentitycharts`
--

CREATE TABLE IF NOT EXISTS `legalentitycharts` (
  `LEGALENTITYCHARTSID` bigint(20) NOT NULL,
  `CHARTNAME` varchar(255) DEFAULT NULL,
  `LINENUM` int(11) DEFAULT NULL,
  `COA_COAID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `legalentitycharts`
--

INSERT INTO `legalentitycharts` (`LEGALENTITYCHARTSID`, `CHARTNAME`, `LINENUM`, `COA_COAID`) VALUES
(8301, 'Knsfshnfq', 0, 6151),
(8302, 'Rfsfljrjsy', 1, 6301),
(8303, 'Knsfshnfq', 0, 6201),
(8304, 'Rfsfljrjsy', 1, 6301),
(8406, 'Rfsfljrjsy', 2, 6301),
(8404, 'Knsfshnfq 6', 0, 6151),
(8405, 'Knsfshnfq 7', 1, 6201);

-- --------------------------------------------------------

--
-- Table structure for table `legalentity_legalentitycharts`
--

CREATE TABLE IF NOT EXISTS `legalentity_legalentitycharts` (
  `LegalEntity_LEGALENTITYID` bigint(20) NOT NULL,
  `legalEntityCharts_LEGALENTITYCHARTSID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `legalentity_legalentitycharts`
--

INSERT INTO `legalentity_legalentitycharts` (`LegalEntity_LEGALENTITYID`, `legalEntityCharts_LEGALENTITYCHARTSID`) VALUES
(2601, 8301),
(2601, 8302),
(2602, 8303),
(2602, 8304),
(7351, 8406),
(7351, 8404),
(7351, 8405);

-- --------------------------------------------------------

--
-- Table structure for table `sequence`
--

CREATE TABLE IF NOT EXISTS `sequence` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sequence`
--

INSERT INTO `sequence` (`SEQ_NAME`, `SEQ_COUNT`) VALUES
('SEQ_GEN', '10500');

-- --------------------------------------------------------

--
-- Table structure for table `taccount`
--

CREATE TABLE IF NOT EXISTS `taccount` (
  `TACCOUNTID` bigint(20) NOT NULL,
  `COL` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ROW` int(11) DEFAULT NULL,
  `COA_COAID` bigint(20) DEFAULT NULL,
  `GL_GLID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taccount`
--

INSERT INTO `taccount` (`TACCOUNTID`, `COL`, `NAME`, `ROW`, `COA_COAID`, `GL_GLID`) VALUES
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
(10401, 0, 'Rfwpjyfgqj Xjhzwnynjx', 1, 6151, 6501),
(10402, 5, 'Kncji Fxxjyx', 1, 6151, 8701);

-- --------------------------------------------------------

--
-- Table structure for table `taccount_corrcx`
--

CREATE TABLE IF NOT EXISTS `taccount_corrcx` (
  `TAccount_TACCOUNTID` bigint(20) DEFAULT NULL,
  `VALUE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taccount_corrcx`
--

INSERT INTO `taccount_corrcx` (`TAccount_TACCOUNTID`, `VALUE`) VALUES
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
(10402, 1);

-- --------------------------------------------------------

--
-- Table structure for table `taccount_corrdx`
--

CREATE TABLE IF NOT EXISTS `taccount_corrdx` (
  `TAccount_TACCOUNTID` bigint(20) DEFAULT NULL,
  `VALUE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taccount_corrdx`
--

INSERT INTO `taccount_corrdx` (`TAccount_TACCOUNTID`, `VALUE`) VALUES
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
(10401, 1);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE IF NOT EXISTS `transaction` (
  `TRANSACTIONID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ROW` int(11) DEFAULT NULL,
  `CX_TACCOUNTID` bigint(20) DEFAULT NULL,
  `DX_TACCOUNTID` bigint(20) DEFAULT NULL,
  `COA_COAID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`TRANSACTIONID`, `DESCRIPTION`, `ROW`, `CX_TACCOUNTID`, `DX_TACCOUNTID`, `COA_COAID`) VALUES
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
(10403, 'opmpqopopoo;pqpqp', 1, 10401, 10402, 6151);

-- --------------------------------------------------------

--
-- Table structure for table `transactionsmodel`
--

CREATE TABLE IF NOT EXISTS `transactionsmodel` (
  `TRANSACTIONSMODELID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `LGLENTITY_LEGALENTITYID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactionsmodel`
--

INSERT INTO `transactionsmodel` (`TRANSACTIONSMODELID`, `NAME`, `LGLENTITY_LEGALENTITYID`) VALUES
(1108, 'Ljsjwfq Jsyjwuwnxj Rtijq', 2601),
(2204, 'Nsajxyrjsy Rtijq', 2601),
(3954, 'Xufwyfs Rtijq', 2602),
(8865, 'Ejxy Jsyjwuwnxj', 7351);

-- --------------------------------------------------------

--
-- Table structure for table `transactionsmodel_taccount`
--

CREATE TABLE IF NOT EXISTS `transactionsmodel_taccount` (
  `TransactionsModel_TRANSACTIONSMODELID` bigint(20) NOT NULL,
  `taccounts_TACCOUNTID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactionsmodel_taccount`
--

INSERT INTO `transactionsmodel_taccount` (`TransactionsModel_TRANSACTIONSMODELID`, `taccounts_TACCOUNTID`) VALUES
(8865, 8851),
(8865, 8852),
(8865, 8853),
(8865, 8854),
(8865, 8855),
(8865, 8856),
(8865, 8857),
(8865, 8858),
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
(2204, 10401),
(2204, 10402);

-- --------------------------------------------------------

--
-- Table structure for table `transactionsmodel_transaction`
--

CREATE TABLE IF NOT EXISTS `transactionsmodel_transaction` (
  `TransactionsModel_TRANSACTIONSMODELID` bigint(20) NOT NULL,
  `transactions_TRANSACTIONID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactionsmodel_transaction`
--

INSERT INTO `transactionsmodel_transaction` (`TransactionsModel_TRANSACTIONSMODELID`, `transactions_TRANSACTIONID`) VALUES
(8865, 8859),
(8865, 8860),
(8865, 8861),
(8865, 8862),
(8865, 8863),
(8865, 8864),
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
(2204, 10403);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `coa`
--
ALTER TABLE `coa`
  ADD PRIMARY KEY (`COAID`), ADD KEY `FK_COA_CURRENCY_CURRENCYID` (`CURRENCY_CURRENCYID`);

--
-- Indexes for table `currency`
--
ALTER TABLE `currency`
  ADD PRIMARY KEY (`CURRENCYID`);

--
-- Indexes for table `gl`
--
ALTER TABLE `gl`
  ADD PRIMARY KEY (`GLID`), ADD KEY `FK_GL_COA_COAID` (`COA_COAID`);

--
-- Indexes for table `glanalytics`
--
ALTER TABLE `glanalytics`
  ADD PRIMARY KEY (`GLANALYTICSID`);

--
-- Indexes for table `gl_glanalytics`
--
ALTER TABLE `gl_glanalytics`
  ADD PRIMARY KEY (`GL_GLID`,`analytics_GLANALYTICSID`), ADD KEY `GLCCNTGLANALYTICSnlytcsGLANALYTICSID` (`analytics_GLANALYTICSID`);

--
-- Indexes for table `legalentity`
--
ALTER TABLE `legalentity`
  ADD PRIMARY KEY (`LEGALENTITYID`);

--
-- Indexes for table `sequence`
--
ALTER TABLE `sequence`
  ADD PRIMARY KEY (`SEQ_NAME`);

--
-- Indexes for table `taccount`
--
ALTER TABLE `taccount`
  ADD PRIMARY KEY (`TACCOUNTID`);

--
-- Indexes for table `taccount_corrcx`
--
ALTER TABLE `taccount_corrcx`
  ADD KEY `FK_TAccount_CORRCX_TAccount_TACCOUNTID` (`TAccount_TACCOUNTID`);

--
-- Indexes for table `taccount_corrdx`
--
ALTER TABLE `taccount_corrdx`
  ADD KEY `FK_TAccount_CORRDX_TAccount_TACCOUNTID` (`TAccount_TACCOUNTID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`TRANSACTIONID`), ADD KEY `FK_TRANSACTION_CX_TACCOUNTID` (`CX_TACCOUNTID`), ADD KEY `FK_TRANSACTION_DX_TACCOUNTID` (`DX_TACCOUNTID`);

--
-- Indexes for table `transactionsmodel`
--
ALTER TABLE `transactionsmodel`
  ADD PRIMARY KEY (`TRANSACTIONSMODELID`), ADD KEY `FK_TRANSACTIONSMODEL_LGLENTITY_LEGALENTITYID` (`LGLENTITY_LEGALENTITYID`);

--
-- Indexes for table `transactionsmodel_taccount`
--
ALTER TABLE `transactionsmodel_taccount`
  ADD PRIMARY KEY (`TransactionsModel_TRANSACTIONSMODELID`,`taccounts_TACCOUNTID`), ADD KEY `FK_TRANSACTIONSMODEL_TACCOUNT_taccounts_TACCOUNTID` (`taccounts_TACCOUNTID`);

--
-- Indexes for table `transactionsmodel_transaction`
--
ALTER TABLE `transactionsmodel_transaction`
  ADD PRIMARY KEY (`TransactionsModel_TRANSACTIONSMODELID`,`transactions_TRANSACTIONID`), ADD KEY `TRNSCTIONSMODELTRANSACTIONtrnsactionsTRANSACTIONID` (`transactions_TRANSACTIONID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `coa`
--
ALTER TABLE `coa`
ADD CONSTRAINT `FK_COA_CURRENCY_CURRENCYID` FOREIGN KEY (`CURRENCY_CURRENCYID`) REFERENCES `currency` (`CURRENCYID`);

--
-- Constraints for table `gl`
--
ALTER TABLE `gl`
ADD CONSTRAINT `FK_GL_COA_COAID` FOREIGN KEY (`COA_COAID`) REFERENCES `coa` (`COAID`);

--
-- Constraints for table `gl_glanalytics`
--
ALTER TABLE `gl_glanalytics`
ADD CONSTRAINT `GL_GLANALYTICS_GL_GLID` FOREIGN KEY (`GL_GLID`) REFERENCES `gl` (`GLID`),
ADD CONSTRAINT `GLCCNTGLANALYTICSnlytcsGLANALYTICSID` FOREIGN KEY (`analytics_GLANALYTICSID`) REFERENCES `glanalytics` (`GLANALYTICSID`);

--
-- Constraints for table `taccount_corrcx`
--
ALTER TABLE `taccount_corrcx`
ADD CONSTRAINT `FK_TAccount_CORRCX_TAccount_TACCOUNTID` FOREIGN KEY (`TAccount_TACCOUNTID`) REFERENCES `taccount` (`TACCOUNTID`);

--
-- Constraints for table `taccount_corrdx`
--
ALTER TABLE `taccount_corrdx`
ADD CONSTRAINT `FK_TAccount_CORRDX_TAccount_TACCOUNTID` FOREIGN KEY (`TAccount_TACCOUNTID`) REFERENCES `taccount` (`TACCOUNTID`);

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
ADD CONSTRAINT `FK_TRANSACTION_CX_TACCOUNTID` FOREIGN KEY (`CX_TACCOUNTID`) REFERENCES `taccount` (`TACCOUNTID`),
ADD CONSTRAINT `FK_TRANSACTION_DX_TACCOUNTID` FOREIGN KEY (`DX_TACCOUNTID`) REFERENCES `taccount` (`TACCOUNTID`);

--
-- Constraints for table `transactionsmodel`
--
ALTER TABLE `transactionsmodel`
ADD CONSTRAINT `FK_TRANSACTIONSMODEL_LGLENTITY_LEGALENTITYID` FOREIGN KEY (`LGLENTITY_LEGALENTITYID`) REFERENCES `legalentity` (`LEGALENTITYID`);

--
-- Constraints for table `transactionsmodel_taccount`
--
ALTER TABLE `transactionsmodel_taccount`
ADD CONSTRAINT `FK_TRANSACTIONSMODEL_TACCOUNT_taccounts_TACCOUNTID` FOREIGN KEY (`taccounts_TACCOUNTID`) REFERENCES `taccount` (`TACCOUNTID`),
ADD CONSTRAINT `TRNSCTNSMDLTACCOUNTTrnsctnsMdelTRANSACTIONSMODELID` FOREIGN KEY (`TransactionsModel_TRANSACTIONSMODELID`) REFERENCES `transactionsmodel` (`TRANSACTIONSMODELID`);

--
-- Constraints for table `transactionsmodel_transaction`
--
ALTER TABLE `transactionsmodel_transaction`
ADD CONSTRAINT `TRNSCTIONSMODELTRANSACTIONtrnsactionsTRANSACTIONID` FOREIGN KEY (`transactions_TRANSACTIONID`) REFERENCES `transaction` (`TRANSACTIONID`),
ADD CONSTRAINT `TRNSCTNSMDLTRNSACTIONTrnsctnsMdlTRNSACTIONSMODELID` FOREIGN KEY (`TransactionsModel_TRANSACTIONSMODELID`) REFERENCES `transactionsmodel` (`TRANSACTIONSMODELID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
