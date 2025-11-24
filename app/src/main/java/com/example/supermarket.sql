-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2025-11-22 09:27:56
-- 服务器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

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
(1, 2, 'S001', 148, '2025-11-22 16:08:24'),
(2, 2, 'S001', 414, '2025-11-22 16:08:40'),
(3, 2, 'S001', 108, '2025-11-22 16:09:31'),
(4, 2, 'S001', 216, '2025-11-22 16:19:46'),
(5, 2, 'S001', 306, '2025-11-22 16:22:04'),
(6, 2, 'S001', 216, '2025-11-22 16:54:05'),
(7, 2, 'S001', 396, '2025-11-22 16:54:33'),
(8, 2, 'S001', 792, '2025-11-22 17:27:08');

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
(1, 1, 3, 1, 148),
(2, 2, 1, 2, 108),
(3, 2, 2, 1, 198),
(4, 3, 1, 1, 108),
(5, 4, 1, 2, 108),
(6, 5, 1, 1, 108),
(7, 5, 2, 1, 198),
(8, 6, 1, 2, 108),
(9, 7, 2, 2, 198),
(10, 8, 2, 4, 198);

-- --------------------------------------------------------

--
-- 表的结构 `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `store_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `stock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `products`
--

INSERT INTO `products` (`product_id`, `store_id`, `name`, `category`, `price`, `stock`) VALUES
(1, 'S001', 'お〜いお茶 500ml', '飲料', 108, 20),
(2, 'S001', 'コーラ 1.5L', '飲料', 198, 15),
(3, 'S001', 'ポテトチップス', '菓子', 148, 30),
(4, 'S002', 'ヨーグルト 4個', '冷蔵', 198, 18),
(5, 'S002', 'カレー中辛', '食品', 250, 25),
(6, 'S003', '食器用洗剤', '日用品', 180, 40),
(7, 'S003', '冷凍ギョーザ', '冷凍', 298, 35);

-- --------------------------------------------------------

--
-- 表的结构 `stores`
--

CREATE TABLE `stores` (
  `store_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `stores`
--

INSERT INTO `stores` (`store_id`, `name`, `address`, `latitude`, `longitude`) VALUES
('S001', 'スーパーA 新宿店', '東京都新宿区○○1-1-1', 35.69, 139.7),
('S002', 'スーパーB 横浜店', '神奈川県横浜市△△2-2-2', 35.4656, 139.6223),
('S003', 'スーパーC 千葉店', '千葉県千葉市□□3-3-3', 35.6074, 140.1065);

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `user_code` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `user_code`, `password`, `name`, `phone`, `email`, `created_at`) VALUES
(1, 'test01', '123456', 'テストユーザー', '080-1234-5678', 'test@example.com', '2025-11-22 16:05:54'),
(2, 'user01', '123', 'user01', '123-4567-8910', 'user01@test.com', '2025-11-22 16:07:26'),
(3, 'test1', 'test1123456', 'test1', '123-4567-8910', 'test1@test.com', '2025-11-22 17:01:58');

--
-- 转储表的索引
--

--
-- 表的索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_orders_user` (`user_id`),
  ADD KEY `fk_orders_store` (`store_id`);

--
-- 表的索引 `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_order_items_order` (`order_id`),
  ADD KEY `fk_order_items_product` (`product_id`);

--
-- 表的索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `fk_products_store` (`store_id`);

--
-- 表的索引 `stores`
--
ALTER TABLE `stores`
  ADD PRIMARY KEY (`store_id`);

--
-- 表的索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_code` (`user_code`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 限制导出的表
--

--
-- 限制表 `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

--
-- 限制表 `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON UPDATE CASCADE;

--
-- 限制表 `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
