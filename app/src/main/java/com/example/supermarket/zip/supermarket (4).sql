-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2025-12-01 05:31:25
-- 服务器版本： 10.4.28-MariaDB
-- PHP 版本： 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `supermarket`
--

-- --------------------------------------------------------

--
-- 表的结构 `nav_edges`
--

CREATE TABLE `nav_edges` (
  `edge_id` int(11) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `from_node_id` varchar(50) NOT NULL,
  `to_node_id` varchar(50) NOT NULL,
  `distance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `nav_edges`
--

INSERT INTO `nav_edges` (`edge_id`, `store_id`, `from_node_id`, `to_node_id`, `distance`) VALUES
(1, 'TEST01', 'N0', 'N1', NULL),
(2, 'TEST01', 'N1', 'N2', NULL),
(3, 'TEST01', 'N2', 'N3', NULL),
(4, 'TEST01', 'N2', 'N4', NULL),
(5, 'TEST01', 'N3', 'N5', NULL),
(6, 'TEST01', 'N4', 'N6', NULL),
(7, 'TEST01', 'N5', 'N7', NULL),
(8, 'TEST01', 'N6', 'N7', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `nav_nodes`
--

CREATE TABLE `nav_nodes` (
  `node_id` varchar(50) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `is_entrance` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `nav_nodes`
--

INSERT INTO `nav_nodes` (`node_id`, `store_id`, `x`, `y`, `is_entrance`) VALUES
('N0', 'TEST01', 0.1, 0.9, 1),
('N1', 'TEST01', 0.25, 0.8, 0),
('N2', 'TEST01', 0.25, 0.6, 0),
('N3', 'TEST01', 0.75, 0.6, 0),
('N4', 'TEST01', 0.25, 0.4, 0),
('N5', 'TEST01', 0.75, 0.4, 0),
('N6', 'TEST01', 0.25, 0.2, 0),
('N7', 'TEST01', 0.75, 0.2, 0);

-- --------------------------------------------------------

--
-- 表的结构 `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `total` int(11) NOT NULL,
  `ordered_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `store_id`, `total`, `ordered_at`) VALUES
(1, 1, 'TEST01', 1540, '2025-11-28 15:51:19'),
(2, 1, 'TEST01', 1200, '2025-12-01 09:09:00');

-- --------------------------------------------------------

--
-- 表的结构 `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(1, 1, 39, 4, 130),
(2, 1, 20, 1, 600),
(3, 1, 21, 1, 300),
(4, 1, 40, 1, 120),
(5, 2, 20, 2, 600);

-- --------------------------------------------------------

--
-- 表的结构 `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `image_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `products`
--

INSERT INTO `products` (`product_id`, `name`, `category`, `price`, `image_name`) VALUES
(1, 'コーラ 500ml', '飲料', 120, 'cola_500ml'),
(2, 'サイダー 500ml', '飲料', 115, 'cider_500ml'),
(3, '緑茶 600ml', '飲料', 110, 'greentea_600'),
(4, '麦茶 650ml', '飲料', 118, 'mugicha_650'),
(5, 'スポーツドリンク', '飲料', 140, 'sports_drink'),
(6, 'ミルクティー 500ml', '飲料', 150, 'milk_tea_500'),
(7, 'レモンウォーター', '飲料', 130, 'lemon_water'),
(8, 'アップルジュース', '飲料', 130, 'apple_juice'),
(9, 'オレンジジュース', '飲料', 130, 'orange_juice'),
(10, '炭酸水 500ml', '飲料', 100, 'sparkling_water'),
(11, 'カフェオレ', '飲料', 150, 'cafe_au_lait'),
(12, '紅茶 ペットボトル', '飲料', 130, 'tea_bottle'),
(13, '黒ウーロン茶 500ml', '飲料', 160, 'oolong_tea'),
(14, 'カルピスウォーター', '飲料', 150, 'calpis_water'),
(15, 'コーヒー微糖', '飲料', 130, 'coffee_sugar'),
(16, 'コーヒー無糖', '飲料', 130, 'coffee_black'),
(17, 'エナジードリンク', '飲料', 210, 'energy_drink'),
(18, 'ぶどうジュース', '飲料', 140, 'grape_juice'),
(19, '水 2L', '飲料', 90, 'water_2l'),
(20, '白米 2kg', '食品', 600, 'rice_2kg'),
(21, 'パスタ 1kg', '食品', 300, 'pasta_1kg'),
(22, 'うどん 3食', '食品', 180, 'udon_3p'),
(23, 'そば 3食', '食品', 180, 'soba_3p'),
(24, 'レトルトカレー 中辛', '食品', 200, 'curry_medium'),
(25, 'レトルト親子丼', '食品', 210, 'oyakodon'),
(26, 'わかめスープ', '食品', 150, 'wakame_soup'),
(27, '味噌汁 パック', '食品', 160, 'miso_soup'),
(28, 'インスタントラーメン 醤油', '食品', 130, 'ramen_shoyu'),
(29, 'インスタントラーメン 味噌', '食品', 130, 'ramen_miso'),
(30, '乾燥わかめ', '食品', 150, 'wakame_dry'),
(31, 'カップスープ', '食品', 140, 'cup_soup'),
(32, 'パン 6枚', '食品', 150, 'bread_6'),
(33, 'カップ焼きそば', '食品', 180, 'yakisoba_cup'),
(34, '親子丼の素', '食品', 190, 'oyakodon_base'),
(35, 'シリアル', '食品', 300, 'cereal'),
(36, 'チキンスープ', '食品', 200, 'chicken_soup'),
(37, 'ポタージュスープ', '食品', 200, 'potage_soup'),
(38, '冷やし中華', '食品', 250, 'hiyashi_chuka'),
(39, 'チョコレート', '菓子', 130, 'chocolate'),
(40, 'ビスケット', '菓子', 120, 'biscuit'),
(41, 'ポテトチップス', '菓子', 150, 'potato_chips'),
(42, 'プリッツ', '菓子', 140, 'pretz'),
(43, 'ポッキー', '菓子', 150, 'pocky'),
(44, 'グミ ぶどう味', '菓子', 120, 'gummy_grape'),
(45, 'おせんべい', '菓子', 130, 'senbei'),
(46, 'ラムネ', '菓子', 100, 'ramune'),
(47, 'キャンディミックス', '菓子', 140, 'candy_mix'),
(48, 'チョコクッキー', '菓子', 150, 'choco_cookie'),
(49, 'カスタードケーキ', '菓子', 160, 'custard_cake'),
(50, 'キャラメル', '菓子', 110, 'caramel'),
(51, 'ソフトキャンディ', '菓子', 120, 'soft_candy'),
(52, 'チョコチップクッキー', '菓子', 140, 'chocochip_cookie'),
(53, 'クラッカー', '菓子', 130, 'cracker'),
(54, 'アーモンドチョコ', '菓子', 160, 'almond_choco'),
(55, 'バタークッキー', '菓子', 150, 'butter_cookie'),
(56, 'あんドーナツ', '菓子', 150, 'an_donut'),
(57, 'たい焼き', '菓子', 170, 'taiyaki'),
(58, '食塩', '調味料', 90, 'salt'),
(59, '砂糖', '調味料', 120, 'sugar'),
(60, '醤油', '調味料', 180, 'soy_sauce'),
(61, 'みりん', '調味料', 180, 'mirin'),
(62, '料理酒', '調味料', 200, 'cooking_sake'),
(63, 'コンソメ', '調味料', 180, 'consomme'),
(64, 'ナンプラー', '調味料', 250, 'nampla'),
(65, 'ごま油', '調味料', 240, 'sesame_oil'),
(66, 'オリーブオイル', '調味料', 260, 'olive_oil'),
(67, 'こしょう', '調味料', 150, 'pepper'),
(68, '鶏ガラスープ', '調味料', 190, 'chicken_stock'),
(69, 'カレーパウダー', '調味料', 200, 'curry_powder'),
(70, '中華だし', '調味料', 210, 'chinese_stock'),
(71, 'バターソース', '調味料', 230, 'butter_sauce'),
(72, 'ハーブソルト', '調味料', 220, 'herb_salt'),
(73, 'だしの素', '調味料', 180, 'dashi'),
(74, 'ドレッシング 和風', '調味料', 220, 'dressing_japanese'),
(75, 'ドレッシング ごま', '調味料', 220, 'dressing_sesame'),
(76, 'ドレッシング フレンチ', '調味料', 220, 'dressing_french'),
(77, '食器用洗剤', '日用品', 200, 'dish_soap'),
(78, '洗濯洗剤', '日用品', 320, 'laundry_detergent'),
(79, '柔軟剤', '日用品', 300, 'fabric_softener'),
(80, 'ティッシュ 5箱', '日用品', 300, 'tissue_5'),
(81, 'トイレットペーパー 12ロール', '日用品', 350, 'toiletpaper_12'),
(82, 'スポンジセット', '日用品', 150, 'sponge_set'),
(83, 'ゴミ袋 45L', '日用品', 190, 'trashbag_45'),
(84, '除菌シート', '日用品', 160, 'clean_sheet'),
(85, '歯ブラシ', '日用品', 120, 'toothbrush'),
(86, '歯磨き粉', '日用品', 180, 'toothpaste'),
(87, 'シャンプー', '日用品', 380, 'shampoo'),
(88, 'ボディソープ', '日用品', 360, 'bodysoap'),
(89, 'カミソリ', '日用品', 260, 'razor'),
(90, '綿棒', '日用品', 120, 'cotton_stick'),
(91, '消臭スプレー', '日用品', 350, 'deodorant_spray'),
(92, '除湿剤', '日用品', 200, 'humidity_absorber'),
(93, 'ラップ', '日用品', 160, 'wrap'),
(94, 'アルミホイル', '日用品', 200, 'aluminum_foil'),
(95, 'マスク 30枚', '日用品', 450, 'mask_30'),
(96, '牛乳 1L', '冷蔵', 180, 'milk_1l'),
(97, 'ヨーグルト', '冷蔵', 120, 'yogurt'),
(98, 'バター', '冷蔵', 260, 'butter'),
(99, 'スライスチーズ', '冷蔵', 220, 'cheese_slice'),
(100, '豆腐 300g', '冷蔵', 100, 'tofu_300'),
(101, '納豆 3P', '冷蔵', 130, 'natto_3p'),
(102, 'キムチ', '冷蔵', 180, 'kimchi'),
(103, '生ハム', '冷蔵', 280, 'nama_ham'),
(104, 'チルド餃子', '冷蔵', 210, 'gyoza_chilled'),
(105, 'チルド焼売', '冷蔵', 220, 'shumai_chilled'),
(106, 'プリン', '冷蔵', 130, 'pudding'),
(107, '生クリーム', '冷蔵', 250, 'cream'),
(108, '飲むヨーグルト', '冷蔵', 140, 'drink_yogurt'),
(109, 'チルド焼き鳥', '冷蔵', 260, 'yakitori_chilled'),
(110, 'さけフレーク', '冷蔵', 280, 'sake_flake'),
(111, '半熟卵 2個', '冷蔵', 160, 'hanjuku_egg'),
(112, '豆乳 1L', '冷蔵', 180, 'soymilk_1l'),
(113, 'チーズボール', '冷蔵', 180, 'cheese_ball'),
(114, '甘酒', '冷蔵', 200, 'amazake'),
(115, '冷凍うどん', '冷凍', 120, 'frozen_udon'),
(116, '冷凍ほうれん草', '冷凍', 220, 'frozen_spinach'),
(117, '冷凍枝豆', '冷凍', 200, 'frozen_edamame'),
(118, '冷凍唐揚げ', '冷凍', 330, 'frozen_karaage'),
(119, '冷凍餃子', '冷凍', 250, 'frozen_gyoza'),
(120, '冷凍炒飯', '冷凍', 260, 'frozen_friedrice'),
(121, '冷凍ピザ', '冷凍', 300, 'frozen_pizza'),
(122, '冷凍たこ焼き', '冷凍', 260, 'frozen_takoyaki'),
(123, '冷凍ラーメン', '冷凍', 280, 'frozen_ramen'),
(124, '冷凍パスタ', '冷凍', 260, 'frozen_pasta'),
(125, '冷凍グラタン', '冷凍', 300, 'frozen_gratin'),
(126, '冷凍エビフライ', '冷凍', 350, 'frozen_ebifry'),
(127, '冷凍肉まん', '冷凍', 260, 'frozen_nikuman'),
(128, '冷凍焼売', '冷凍', 250, 'frozen_shumai'),
(129, '冷凍春巻き', '冷凍', 240, 'frozen_harumaki'),
(130, '冷凍オムライス', '冷凍', 300, 'frozen_omurice'),
(131, '冷凍チキン南蛮', '冷凍', 330, 'frozen_namban'),
(132, '冷凍三色丼', '冷凍', 350, 'frozen_3don'),
(133, '冷凍ビーフカレー', '冷凍', 350, 'frozen_beef_curry'),
(134, '延長コード', 'その他', 500, 'extension_cord'),
(135, '電池 単3 4本', 'その他', 280, 'battery_aa4'),
(136, '簡易ライト', 'その他', 300, 'simple_light'),
(137, 'クリップセット', 'その他', 120, 'clip_set'),
(138, 'ガムテープ', 'その他', 180, 'tape_brown'),
(139, 'ノート 3冊セット', 'その他', 220, 'notebook_set'),
(140, 'ボールペンセット', 'その他', 230, 'pen_set'),
(141, '封筒 20枚入', 'その他', 160, 'envelope_20'),
(142, '文房具セット', 'その他', 350, 'stationery_set'),
(143, 'USB充電ケーブル', 'その他', 350, 'usb_cable'),
(144, 'ペットシーツ', 'その他', 420, 'pet_sheet'),
(145, 'ミニ工具セット', 'その他', 550, 'tools_set'),
(146, 'キーホルダー', 'その他', 200, 'keyholder'),
(147, 'USBメモリ 16GB', 'その他', 600, 'usb_16gb'),
(148, '携帯ライト', 'その他', 300, 'portable_light'),
(149, 'カッター', 'その他', 150, 'cutter'),
(150, 'ホチキス', 'その他', 150, 'stapler');

-- --------------------------------------------------------

--
-- 表的结构 `shelf_access_points`
--

CREATE TABLE `shelf_access_points` (
  `access_point_id` varchar(50) NOT NULL,
  `shelf_id` varchar(50) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `nearest_node_id` varchar(50) NOT NULL,
  `side` enum('TOP','BOTTOM','LEFT','RIGHT') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `shelf_access_points`
--

INSERT INTO `shelf_access_points` (`access_point_id`, `shelf_id`, `store_id`, `x`, `y`, `nearest_node_id`, `side`) VALUES
('AP_A', 'A', 'TEST01', 0.2, 0.15, 'N6', 'BOTTOM'),
('AP_B', 'B', 'TEST01', 0.4, 0.15, 'N6', 'BOTTOM'),
('AP_C', 'C', 'TEST01', 0.6, 0.15, 'N7', 'BOTTOM'),
('AP_D', 'D', 'TEST01', 0.8, 0.15, 'N7', 'BOTTOM'),
('AP_E', 'E', 'TEST01', 0.35, 0.45, 'N4', 'BOTTOM'),
('AP_F', 'F', 'TEST01', 0.65, 0.45, 'N5', 'BOTTOM'),
('AP_G', 'G', 'TEST01', 0.35, 0.55, 'N2', 'TOP'),
('AP_H', 'H', 'TEST01', 0.65, 0.55, 'N3', 'TOP');

-- --------------------------------------------------------

--
-- 表的结构 `shelves`
--

CREATE TABLE `shelves` (
  `shelf_id` varchar(50) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `width` float NOT NULL,
  `height` float NOT NULL,
  `is_against_wall` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `shelves`
--

INSERT INTO `shelves` (`shelf_id`, `store_id`, `x`, `y`, `width`, `height`, `is_against_wall`) VALUES
('A', 'TEST01', 0.2, 0.1, 0.18, 0.06, 1),
('B', 'TEST01', 0.4, 0.1, 0.18, 0.06, 1),
('C', 'TEST01', 0.6, 0.1, 0.18, 0.06, 1),
('D', 'TEST01', 0.8, 0.1, 0.18, 0.06, 1),
('E', 'TEST01', 0.35, 0.4, 0.3, 0.06, 0),
('F', 'TEST01', 0.65, 0.4, 0.3, 0.06, 0),
('G', 'TEST01', 0.35, 0.6, 0.3, 0.06, 0),
('H', 'TEST01', 0.65, 0.6, 0.3, 0.06, 0);

-- --------------------------------------------------------

--
-- 表的结构 `stores`
--

CREATE TABLE `stores` (
  `store_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `stores`
--

INSERT INTO `stores` (`store_id`, `name`, `address`, `latitude`, `longitude`) VALUES
('TEST01', 'テストスーパー', '東京都品川区テスト1-2-3', 35, 139);

-- --------------------------------------------------------

--
-- 表的结构 `store_products`
--

CREATE TABLE `store_products` (
  `id` int(11) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `product_id` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  `shelf_id` varchar(50) DEFAULT NULL,
  `access_point_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `store_products`
--

INSERT INTO `store_products` (`id`, `store_id`, `product_id`, `stock`, `shelf_id`, `access_point_id`) VALUES
(158, 'TEST01', 1, 20, 'A', 'AP_A'),
(159, 'TEST01', 2, 20, 'A', 'AP_A'),
(160, 'TEST01', 3, 20, 'A', 'AP_A'),
(161, 'TEST01', 4, 20, 'A', 'AP_A'),
(162, 'TEST01', 5, 20, 'A', 'AP_A'),
(163, 'TEST01', 6, 20, 'A', 'AP_A'),
(164, 'TEST01', 7, 20, 'A', 'AP_A'),
(165, 'TEST01', 8, 20, 'A', 'AP_A'),
(166, 'TEST01', 9, 20, 'A', 'AP_A'),
(167, 'TEST01', 10, 20, 'A', 'AP_A'),
(168, 'TEST01', 20, 20, 'B', 'AP_B'),
(169, 'TEST01', 21, 20, 'B', 'AP_B'),
(170, 'TEST01', 22, 20, 'B', 'AP_B'),
(171, 'TEST01', 23, 20, 'B', 'AP_B'),
(172, 'TEST01', 24, 20, 'B', 'AP_B'),
(173, 'TEST01', 25, 20, 'B', 'AP_B'),
(174, 'TEST01', 26, 20, 'B', 'AP_B'),
(175, 'TEST01', 27, 20, 'B', 'AP_B'),
(176, 'TEST01', 28, 20, 'B', 'AP_B'),
(177, 'TEST01', 29, 20, 'B', 'AP_B'),
(178, 'TEST01', 39, 20, 'C', 'AP_C'),
(179, 'TEST01', 40, 20, 'C', 'AP_C'),
(180, 'TEST01', 41, 20, 'C', 'AP_C'),
(181, 'TEST01', 42, 20, 'C', 'AP_C'),
(182, 'TEST01', 43, 20, 'C', 'AP_C'),
(183, 'TEST01', 44, 20, 'C', 'AP_C'),
(184, 'TEST01', 45, 20, 'C', 'AP_C'),
(185, 'TEST01', 46, 20, 'C', 'AP_C'),
(186, 'TEST01', 47, 20, 'C', 'AP_C'),
(187, 'TEST01', 48, 20, 'C', 'AP_C'),
(188, 'TEST01', 58, 20, 'D', 'AP_D'),
(189, 'TEST01', 59, 20, 'D', 'AP_D'),
(190, 'TEST01', 60, 20, 'D', 'AP_D'),
(191, 'TEST01', 61, 20, 'D', 'AP_D'),
(192, 'TEST01', 62, 20, 'D', 'AP_D'),
(193, 'TEST01', 63, 20, 'D', 'AP_D'),
(194, 'TEST01', 64, 20, 'D', 'AP_D'),
(195, 'TEST01', 65, 20, 'D', 'AP_D'),
(196, 'TEST01', 66, 20, 'D', 'AP_D'),
(197, 'TEST01', 67, 20, 'D', 'AP_D'),
(198, 'TEST01', 77, 20, 'E', 'AP_E'),
(199, 'TEST01', 78, 20, 'E', 'AP_E'),
(200, 'TEST01', 79, 20, 'E', 'AP_E'),
(201, 'TEST01', 80, 20, 'E', 'AP_E'),
(202, 'TEST01', 81, 20, 'E', 'AP_E'),
(203, 'TEST01', 82, 20, 'E', 'AP_E'),
(204, 'TEST01', 83, 20, 'E', 'AP_E'),
(205, 'TEST01', 84, 20, 'E', 'AP_E'),
(206, 'TEST01', 85, 20, 'E', 'AP_E'),
(207, 'TEST01', 86, 20, 'E', 'AP_E'),
(208, 'TEST01', 96, 20, 'F', 'AP_F'),
(209, 'TEST01', 97, 20, 'F', 'AP_F'),
(210, 'TEST01', 98, 20, 'F', 'AP_F'),
(211, 'TEST01', 99, 20, 'F', 'AP_F'),
(212, 'TEST01', 100, 20, 'F', 'AP_F'),
(213, 'TEST01', 101, 20, 'F', 'AP_F'),
(214, 'TEST01', 102, 20, 'F', 'AP_F'),
(215, 'TEST01', 103, 20, 'F', 'AP_F'),
(216, 'TEST01', 104, 20, 'F', 'AP_F'),
(217, 'TEST01', 105, 20, 'F', 'AP_F'),
(218, 'TEST01', 115, 20, 'G', 'AP_G'),
(219, 'TEST01', 116, 20, 'G', 'AP_G'),
(220, 'TEST01', 117, 20, 'G', 'AP_G'),
(221, 'TEST01', 118, 20, 'G', 'AP_G'),
(222, 'TEST01', 119, 20, 'G', 'AP_G'),
(223, 'TEST01', 120, 20, 'G', 'AP_G'),
(224, 'TEST01', 121, 20, 'G', 'AP_G'),
(225, 'TEST01', 122, 20, 'G', 'AP_G'),
(226, 'TEST01', 123, 20, 'G', 'AP_G'),
(227, 'TEST01', 124, 20, 'G', 'AP_G'),
(228, 'TEST01', 134, 20, 'H', 'AP_H'),
(229, 'TEST01', 135, 20, 'H', 'AP_H'),
(230, 'TEST01', 136, 20, 'H', 'AP_H'),
(231, 'TEST01', 137, 20, 'H', 'AP_H'),
(232, 'TEST01', 138, 20, 'H', 'AP_H');

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `userid` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `userid`, `password`, `username`, `gender`, `phone`, `email`) VALUES
(1, 'user01', '123', 'user01', '男性', '123-4567-8910', 'user01@test.com');

--
-- 转储表的索引
--

--
-- 表的索引 `nav_edges`
--
ALTER TABLE `nav_edges`
  ADD PRIMARY KEY (`edge_id`),
  ADD KEY `store_id` (`store_id`),
  ADD KEY `from_node_id` (`from_node_id`),
  ADD KEY `to_node_id` (`to_node_id`);

--
-- 表的索引 `nav_nodes`
--
ALTER TABLE `nav_nodes`
  ADD PRIMARY KEY (`node_id`),
  ADD KEY `store_id` (`store_id`);

--
-- 表的索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `store_id` (`store_id`);

--
-- 表的索引 `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- 表的索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- 表的索引 `shelf_access_points`
--
ALTER TABLE `shelf_access_points`
  ADD PRIMARY KEY (`access_point_id`),
  ADD KEY `shelf_id` (`shelf_id`),
  ADD KEY `store_id` (`store_id`),
  ADD KEY `nearest_node_id` (`nearest_node_id`);

--
-- 表的索引 `shelves`
--
ALTER TABLE `shelves`
  ADD PRIMARY KEY (`shelf_id`),
  ADD KEY `store_id` (`store_id`);

--
-- 表的索引 `stores`
--
ALTER TABLE `stores`
  ADD PRIMARY KEY (`store_id`);

--
-- 表的索引 `store_products`
--
ALTER TABLE `store_products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `store_id` (`store_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `shelf_id` (`shelf_id`),
  ADD KEY `access_point_id` (`access_point_id`);

--
-- 表的索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `nav_edges`
--
ALTER TABLE `nav_edges`
  MODIFY `edge_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用表AUTO_INCREMENT `store_products`
--
ALTER TABLE `store_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=233;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 限制导出的表
--

--
-- 限制表 `nav_edges`
--
ALTER TABLE `nav_edges`
  ADD CONSTRAINT `ne_from_fk` FOREIGN KEY (`from_node_id`) REFERENCES `nav_nodes` (`node_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ne_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ne_to_fk` FOREIGN KEY (`to_node_id`) REFERENCES `nav_nodes` (`node_id`) ON DELETE CASCADE;

--
-- 限制表 `nav_nodes`
--
ALTER TABLE `nav_nodes`
  ADD CONSTRAINT `nn_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE;

--
-- 限制表 `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `orders_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- 限制表 `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `oi_order_fk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `oi_product_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- 限制表 `shelf_access_points`
--
ALTER TABLE `shelf_access_points`
  ADD CONSTRAINT `sap_node_fk` FOREIGN KEY (`nearest_node_id`) REFERENCES `nav_nodes` (`node_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sap_shelf_fk` FOREIGN KEY (`shelf_id`) REFERENCES `shelves` (`shelf_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sap_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE;

--
-- 限制表 `shelves`
--
ALTER TABLE `shelves`
  ADD CONSTRAINT `sh_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE;

--
-- 限制表 `store_products`
--
ALTER TABLE `store_products`
  ADD CONSTRAINT `sp_product_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sp_store_fk` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
