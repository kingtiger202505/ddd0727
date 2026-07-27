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
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_phone` (`phone`),
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色 ID',
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE `permission` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限 ID',
    `code` VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `type` TINYINT NOT NULL COMMENT '类型：1-菜单 2-按钮',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级 ID',
    `path` VARCHAR(255) COMMENT '路由路径',
    `icon` VARCHAR(50) COMMENT '图标',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE `user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (`user_id`, `role_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE `role_permission` (
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限 ID',
    PRIMARY KEY (`role_id`, `permission_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 商品表
CREATE TABLE `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品 ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `main_image` VARCHAR(255) COMMENT '主图 URL',
    `images` JSON COMMENT '图片列表',
    `category_id` BIGINT COMMENT '分类 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-下架 1-上架',
    `sales_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_seller_id` (`seller_id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品属性表 (如：颜色，尺寸)
CREATE TABLE `product_attribute` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '属性 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `name` VARCHAR(50) NOT NULL COMMENT '属性名',
    `values` JSON NOT NULL COMMENT '属性值集合',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- SKU 表 (库存量单位)
CREATE TABLE `sku` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `sku_code` VARCHAR(50) NOT NULL UNIQUE COMMENT 'SKU 编码',
    `attrs` JSON NOT NULL COMMENT '规格属性 {"颜色":"红","尺寸":"L"}',
    `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
    `cost_price` DECIMAL(10,2) COMMENT '成本价',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `lock_stock` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU 表';

-- 库存表 (总库存管理)
CREATE TABLE `inventory` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存 ID',
    `product_id` BIGINT NOT NULL UNIQUE COMMENT '商品 ID',
    `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `available_quantity` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    `locked_quantity` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `warehouse_code` VARCHAR(50) COMMENT '仓库编码',
    `low_stock_threshold` INT NOT NULL DEFAULT 10 COMMENT '低库存预警阈值',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 订单表
CREATE TABLE `order_info` (
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
    `shipping_address` JSON COMMENT '收货地址',
    `tracking_no` VARCHAR(100) COMMENT '物流单号',
    `shipped_at` DATETIME COMMENT '发货时间',
    `completed_at` DATETIME COMMENT '完成时间',
    `cancelled_at` DATETIME COMMENT '取消时间',
    `cancel_reason` VARCHAR(255) COMMENT '取消原因',
    `remark` TEXT COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
    `sku_id` BIGINT COMMENT 'SKU ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称 (快照)',
    `product_image` VARCHAR(255) COMMENT '商品图片 (快照)',
    `sku_attrs` JSON COMMENT 'SKU 属性 (快照)',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 报价单表
CREATE TABLE `quotation` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报价单 ID',
    `quotation_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '报价单号',
    `buyer_id` BIGINT NOT NULL COMMENT '买家 ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家 ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `sku_id` BIGINT COMMENT 'SKU ID',
    `quantity` INT NOT NULL COMMENT '采购数量',
    `target_price` DECIMAL(10,2) NOT NULL COMMENT '目标价格',
    `quoted_price` DECIMAL(10,2) COMMENT '报价价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/APPROVED/REJECTED/CANCELLED',
    `remark` TEXT COMMENT '备注',
    `approved_at` DATETIME COMMENT '批准时间',
    `rejected_at` DATETIME COMMENT '拒绝时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_buyer_id` (`buyer_id`),
    INDEX `idx_seller_id` (`seller_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价单表';

-- 购物车表
CREATE TABLE `cart` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `selected` TINYINT NOT NULL DEFAULT 1 COMMENT '是否选中：0-否 1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- ============ 初始化数据 ============

-- 角色数据
INSERT INTO `role` (`code`, `name`, `description`) VALUES
('ADMIN', '超级管理员', '拥有所有权限'),
('OPERATOR', '运营人员', '商品和订单管理'),
('SELLER', '商家', '店铺管理'),
('BUYER', '普通用户', '小程序端用户');

-- 权限数据
INSERT INTO `permission` (`code`, `name`, `type`, `parent_id`, `path`, `icon`, `sort`) VALUES
-- 系统管理
('system', '系统管理', 1, 0, '/system', 'setting', 100),
('system:role', '角色管理', 1, 'system', '/system/role', 'user', 101),
('system:role:add', '新增角色', 2, 'system:role', NULL, NULL, 0),
('system:role:edit', '编辑角色', 2, 'system:role', NULL, NULL, 0),
('system:role:delete', '删除角色', 2, 'system:role', NULL, NULL, 0),
('system:user', '用户管理', 1, 'system', '/system/user', 'users', 102),
('system:user:add', '新增用户', 2, 'system:user', NULL, NULL, 0),
('system:user:edit', '编辑用户', 2, 'system:user', NULL, NULL, 0),
-- 商品管理
('product', '商品管理', 1, 0, '/product', 'shopping-bag', 10),
('product:list', '商品列表', 1, 'product', '/product/list', 'list', 11),
('product:add', '新增商品', 2, 'product:list', NULL, NULL, 0),
('product:edit', '编辑商品', 2, 'product:list', NULL, NULL, 0),
('product:delete', '删除商品', 2, 'product:list', NULL, NULL, 0),
('product:sku', 'SKU 管理', 1, 'product', '/product/sku', 'grid', 12),
('product:sku:add', '新增 SKU', 2, 'product:sku', NULL, NULL, 0),
('product:sku:edit', '编辑 SKU', 2, 'product:sku', NULL, NULL, 0),
-- 库存管理
('inventory', '库存管理', 1, 0, '/inventory', 'box', 20),
('inventory:list', '库存列表', 1, 'inventory', '/inventory/list', 'list', 21),
('inventory:adjust', '库存调整', 2, 'inventory:list', NULL, NULL, 0),
-- 订单管理
('order', '订单管理', 1, 0, '/order', 'shopping-cart', 30),
('order:list', '订单列表', 1, 'order', '/order/list', 'list', 31),
('order:detail', '订单详情', 2, 'order:list', NULL, NULL, 0),
('order:ship', '订单发货', 2, 'order:list', NULL, NULL, 0),
-- 报价管理
('quotation', '报价管理', 1, 0, '/quotation', 'document', 40),
('quotation:list', '报价列表', 1, 'quotation', '/quotation/list', 'list', 41),
('quotation:approve', '报价审批', 2, 'quotation:list', NULL, NULL, 0);

-- 管理员用户 (密码：admin123，BCrypt 加密)
INSERT INTO `user` (`username`, `password`, `phone`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138000', 1);

-- 分配管理员角色
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 分配管理员权限 (ADMIN 角色拥有所有权限)
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission`;

-- 测试商品数据
INSERT INTO `product` (`name`, `description`, `main_image`, `seller_id`, `status`) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro 芯片，钛金属设计', 'https://example.com/iphone15pro.jpg', 1, 1),
('MacBook Pro 14', 'M3 Pro 芯片，18GB+512GB，深空黑色', 'https://example.com/macbookpro14.jpg', 1, 1),
('AirPods Pro 2', '主动降噪无线耳机，USB-C 充电盒', 'https://example.com/airpodspro2.jpg', 1, 1);

-- 测试 SKU 数据
INSERT INTO `sku` (`product_id`, `sku_code`, `attrs`, `price`, `cost_price`, `stock`) VALUES
(1, 'IP15P-NT-256', '{"颜色":"原色钛金属","容量":"256GB"}', 7999.00, 6500.00, 100),
(1, 'IP15P-BT-256', '{"颜色":"蓝色钛金属","容量":"256GB"}', 7999.00, 6500.00, 80),
(1, 'IP15P-NT-512', '{"颜色":"原色钛金属","容量":"512GB"}', 9999.00, 8200.00, 50),
(2, 'MBP14-M3P-18G-512', '{"芯片":"M3 Pro","内存":"18GB","硬盘":"512GB"}', 14999.00, 12000.00, 30),
(2, 'MBP14-M3M-18G-512', '{"芯片":"M3 Max","内存":"18GB","硬盘":"512GB"}', 18999.00, 15500.00, 20),
(3, 'APP2-USBC-W', '{"颜色":"白色","充电盒":"USB-C"}', 1899.00, 1400.00, 200),
(3, 'APP2-USBC-B', '{"颜色":"黑色","充电盒":"USB-C"}', 1899.00, 1400.00, 180);

-- 测试库存数据
INSERT INTO `inventory` (`product_id`, `total_quantity`, `available_quantity`, `locked_quantity`, `low_stock_threshold`) VALUES
(1, 230, 230, 0, 20),
(2, 50, 50, 0, 10),
(3, 380, 380, 0, 30);
