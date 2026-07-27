-- ============================================
-- 电商报价议价系统数据库表结构
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码 (BCrypt 加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `role` VARCHAR(20) NOT NULL DEFAULT 'BUYER' COMMENT '角色：BUYER, SELLER, ADMIN',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 商品表
CREATE TABLE IF NOT EXISTS `products` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT DEFAULT NULL COMMENT '商品描述',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类 ID',
    `category_name` VARCHAR(100) DEFAULT NULL COMMENT '分类名称',
    `base_price` DECIMAL(10,2) NOT NULL COMMENT '基础价格',
    `min_price` DECIMAL(10,2) DEFAULT NULL COMMENT '最低价格',
    `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `unit` VARCHAR(20) DEFAULT '件' COMMENT '单位',
    `images` JSON DEFAULT NULL COMMENT '商品图片 URL 列表',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS `product_categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `level` TINYINT NOT NULL DEFAULT 1 COMMENT '分类层级',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    `icon` VARCHAR(200) DEFAULT NULL COMMENT '分类图标',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- 报价单表
CREATE TABLE IF NOT EXISTS `quotations` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报价单 ID',
    `buyer_id` BIGINT NOT NULL COMMENT '买家 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '报价金额',
    `quantity` INT NOT NULL COMMENT '数量',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING, COUNTER_OFFER, ACCEPTED, REJECTED, CLOSED',
    `message` VARCHAR(500) DEFAULT NULL COMMENT '留言/备注',
    `counter_count` INT NOT NULL DEFAULT 0 COMMENT '还价次数',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价单表';

-- 报价历史记录表
CREATE TABLE IF NOT EXISTS `quotation_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '历史记录 ID',
    `quotation_id` BIGINT NOT NULL COMMENT '报价单 ID',
    `operator_id` BIGINT NOT NULL COMMENT '操作人 ID',
    `operator_role` VARCHAR(20) NOT NULL COMMENT '操作人角色',
    `action` VARCHAR(20) NOT NULL COMMENT '操作类型：CREATE, COUNTER_OFFER, ACCEPT, REJECT, CLOSE',
    `old_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价格',
    `new_price` DECIMAL(10,2) DEFAULT NULL COMMENT '新价格',
    `old_status` VARCHAR(20) DEFAULT NULL COMMENT '原状态',
    `new_status` VARCHAR(20) DEFAULT NULL COMMENT '新状态',
    `message` VARCHAR(500) DEFAULT NULL COMMENT '操作留言',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_quotation_id` (`quotation_id`),
    KEY `idx_operator_id` (`operator_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价历史记录表';

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单 ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `quotation_id` BIGINT DEFAULT NULL COMMENT '关联报价单 ID',
    `buyer_id` BIGINT NOT NULL COMMENT '买家 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `pay_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
    `freight_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态：PENDING_PAYMENT, PAID, SHIPPED, RECEIVED, COMPLETED, CANCELLED, REFUNDED',
    `payment_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式',
    `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `shipping_address` JSON DEFAULT NULL COMMENT '收货地址',
    `tracking_no` VARCHAR(100) DEFAULT NULL COMMENT '物流单号',
    `shipping_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `receive_time` DATETIME DEFAULT NULL COMMENT '收货时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_quotation_id` (`quotation_id`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单商品明细表
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单项 ID',
    `order_id` BIGINT NOT NULL COMMENT '订单 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(200) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL COMMENT '成交单价',
    `quantity` INT NOT NULL COMMENT '购买数量',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品明细表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS `payments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '支付记录 ID',
    `payment_no` VARCHAR(32) NOT NULL COMMENT '支付流水号',
    `order_id` BIGINT NOT NULL COMMENT '订单 ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '支付用户 ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `payment_method` VARCHAR(20) NOT NULL COMMENT '支付方式：ALIPAY, WECHAT, BANK_TRANSFER',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态：PENDING, SUCCESS, FAILED, REFUNDED',
    `transaction_id` VARCHAR(100) DEFAULT NULL COMMENT '第三方交易流水号',
    `payment_time` DATETIME DEFAULT NULL COMMENT '支付成功时间',
    `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
    `refund_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '退款金额',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- 消息通知表
CREATE TABLE IF NOT EXISTS `notifications` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知 ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户 ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT NOT NULL COMMENT '通知内容',
    `type` VARCHAR(20) NOT NULL COMMENT '通知类型：QUOTATION, ORDER, PAYMENT, SYSTEM',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联业务 ID',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_configs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置 ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_type` VARCHAR(20) DEFAULT 'STRING' COMMENT '配置类型：STRING, NUMBER, BOOLEAN, JSON',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
    `is_public` TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开：0-私有，1-公开',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 初始化默认数据
INSERT INTO `product_categories` (`name`, `level`, `sort_order`) VALUES 
('电子产品', 1, 1),
('服装服饰', 1, 2),
('家居用品', 1, 3),
('食品饮料', 1, 4),
('图书文具', 1, 5);

INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `description`, `is_public`) VALUES
('min_quotation_amount', '1.00', 'NUMBER', '最小报价金额', 0),
('max_quotation_amount', '1000000.00', 'NUMBER', '最大报价金额', 0),
('max_counter_offer_times', '5', 'NUMBER', '最大还价次数', 1),
('quotation_expire_hours', '72', 'NUMBER', '报价单过期时间 (小时)', 1),
('order_auto_cancel_minutes', '30', 'NUMBER', '订单自动取消时间 (分钟)', 0),
('system_maintenance', 'false', 'BOOLEAN', '系统维护模式', 1);
