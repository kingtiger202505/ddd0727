-- 电商报价议价系统数据库表结构
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS quotation_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE quotation_system;

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码 (加密)',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像 URL',
    `role` VARCHAR(20) NOT NULL DEFAULT 'BUYER' COMMENT '角色：BUYER/SELLER/ADMIN',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_phone` (`phone`),
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品表
CREATE TABLE `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品 ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `main_image` VARCHAR(255) COMMENT '主图 URL',
    `images` TEXT COMMENT '图片列表 (JSON)',
    `category_id` BIGINT COMMENT '分类 ID',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `original_price` DECIMAL(10,2) COMMENT '原价',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-下架 1-上架',
    `sales_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_seller_id` (`seller_id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 库存表
CREATE TABLE `inventory` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存 ID',
    `product_id` BIGINT NOT NULL UNIQUE COMMENT '商品 ID',
    `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `available_quantity` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    `locked_quantity` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `warehouse_code` VARCHAR(50) COMMENT '仓库编码',
    `low_stock_threshold` INT NOT NULL DEFAULT 10 COMMENT '低库存预警阈值',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 报价单表
CREATE TABLE `quotation` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报价单 ID',
    `quotation_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '报价单号',
    `buyer_id` BIGINT NOT NULL COMMENT '买家 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `quantity` INT NOT NULL COMMENT '采购数量',
    `target_price` DECIMAL(10,2) NOT NULL COMMENT '目标价格',
    `quoted_price` DECIMAL(10,2) COMMENT '报价价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/APPROVED/REJECTED/CANCELLED',
    `remark` TEXT COMMENT '备注',
    `approved_at` DATETIME COMMENT '批准时间',
    `rejected_at` DATETIME COMMENT '拒绝时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_buyer_id` (`buyer_id`),
    INDEX `idx_seller_id` (`seller_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价单表';

-- 订单表
CREATE TABLE `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `buyer_id` BIGINT NOT NULL COMMENT '买家 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `quotation_id` BIGINT COMMENT '关联报价单 ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `discount_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
    `shipping_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
    `actual_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '状态：PENDING_PAYMENT/PAID/SHIPPED/COMPLETED/CANCELLED',
    `payment_method` VARCHAR(20) COMMENT '支付方式',
    `payment_time` DATETIME COMMENT '支付时间',
    `shipping_address` TEXT COMMENT '收货地址 (JSON)',
    `tracking_no` VARCHAR(100) COMMENT '物流单号',
    `shipped_at` DATETIME COMMENT '发货时间',
    `completed_at` DATETIME COMMENT '完成时间',
    `cancelled_at` DATETIME COMMENT '取消时间',
    `cancel_reason` VARCHAR(255) COMMENT '取消原因',
    `remark` TEXT COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_buyer_id` (`buyer_id`),
    INDEX `idx_seller_id` (`seller_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品表
CREATE TABLE `order_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单项 ID',
    `order_id` BIGINT NOT NULL COMMENT '订单 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称 (快照)',
    `product_image` VARCHAR(255) COMMENT '商品图片 (快照)',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 购物车表
CREATE TABLE `cart` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `selected` TINYINT NOT NULL DEFAULT 1 COMMENT '是否选中：0-否 1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 收货地址表
CREATE TABLE `address` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省',
    `city` VARCHAR(50) NOT NULL COMMENT '市',
    `district` VARCHAR(50) NOT NULL COMMENT '区',
    `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认：0-否 1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 初始测试数据
INSERT INTO `user` (`username`, `password`, `phone`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138000', 'ADMIN', 1),
('seller1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138001', 'SELLER', 1),
('buyer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138002', 'BUYER', 1);

INSERT INTO `product` (`name`, `description`, `price`, `original_price`, `seller_id`, `status`) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro 芯片', 7999.00, 8999.00, 2, 1),
('MacBook Pro 14', 'M3 Pro 芯片，18GB+512GB', 14999.00, 16999.00, 2, 1),
('AirPods Pro 2', '主动降噪无线耳机', 1899.00, 1999.00, 2, 1);

INSERT INTO `inventory` (`product_id`, `total_quantity`, `available_quantity`, `locked_quantity`, `low_stock_threshold`) VALUES
(1, 100, 100, 0, 10),
(2, 50, 50, 0, 5),
(3, 200, 200, 0, 20);
