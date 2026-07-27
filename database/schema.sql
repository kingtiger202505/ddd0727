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

-- ============================================
-- 角色权限管理相关表
-- ============================================

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色 ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限 ID',
    `code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `type` VARCHAR(20) NOT NULL DEFAULT 'MENU' COMMENT '权限类型：MENU-菜单，BUTTON-按钮，API-接口',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级权限 ID',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '路径/URL',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限 ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ============================================
-- SKU 管理相关表
-- ============================================

-- SKU 表
CREATE TABLE IF NOT EXISTS `sku` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
    `product_id` BIGINT NOT NULL COMMENT '商品 ID',
    `sku_code` VARCHAR(50) NOT NULL COMMENT 'SKU 编码',
    `specs_json` JSON DEFAULT NULL COMMENT '规格组合 JSON',
    `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
    `cost_price` DECIMAL(10,2) DEFAULT NULL COMMENT '成本价',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `locked_stock` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `available_stock` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_code` (`sku_code`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU 表';

-- 库存表
CREATE TABLE IF NOT EXISTS `inventory` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '库存 ID',
    `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
    `warehouse_id` BIGINT DEFAULT 1 COMMENT '仓库 ID',
    `total_stock` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `available_stock` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    `locked_stock` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `damaged_stock` INT NOT NULL DEFAULT 0 COMMENT '残次库存',
    `min_stock` INT NOT NULL DEFAULT 10 COMMENT '最低库存预警线',
    `max_stock` INT DEFAULT NULL COMMENT '最高库存预警线',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号（乐观锁）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_warehouse` (`sku_id`, `warehouse_id`),
    KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- 库存流水表
CREATE TABLE IF NOT EXISTS `inventory_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流水 ID',
    `inventory_id` BIGINT NOT NULL COMMENT '库存 ID',
    `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
    `change_num` INT NOT NULL COMMENT '变动数量（正数入库，负数出库）',
    `before_stock` INT NOT NULL COMMENT '变动前库存',
    `after_stock` INT NOT NULL COMMENT '变动后库存',
    `biz_type` VARCHAR(20) NOT NULL COMMENT '业务类型：PURCHASE-采购，SALE-销售，RETURN-退货，ADJUST-调整',
    `biz_no` VARCHAR(50) DEFAULT NULL COMMENT '业务单号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人 ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_inventory_id` (`inventory_id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_biz_type` (`biz_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水表';

-- 仓库表
CREATE TABLE IF NOT EXISTS `warehouse` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓库 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
    `code` VARCHAR(50) NOT NULL COMMENT '仓库编码',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '仓库地址',
    `manager` VARCHAR(50) DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';

-- 初始化默认数据
INSERT INTO `product_categories` (`name`, `level`, `sort_order`) VALUES 
('电子产品', 1, 1),
('服装服饰', 1, 2),
('家居用品', 1, 3),
('食品饮料', 1, 4),
('图书文具', 1, 5);

-- 初始化默认角色
INSERT INTO `role` (`role_name`, `role_code`, `description`, `status`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1),
('管理员', 'ADMIN', '系统管理员', 1),
('普通用户', 'USER', '普通用户', 1);

-- 初始化默认权限
INSERT INTO `permission` (`code`, `permission_name`, `type`, `parent_id`, `path`, `icon`, `sort`, `status`) VALUES
-- 一级菜单
('system', '系统管理', 'MENU', 0, '/system', 'Setting', 1, 1),
('product_manage', '商品管理', 'MENU', 0, '/product', 'ShoppingCart', 2, 1),
('order_manage', '订单管理', 'MENU', 0, '/order', 'Document', 3, 1),
('inventory_manage', '库存管理', 'MENU', 0, '/inventory', 'Box', 4, 1),
('quotation_manage', '报价管理', 'MENU', 0, '/quotation', 'Money', 5, 1),
('user_manage', '用户管理', 'MENU', 0, '/user', 'User', 6, 1),
('role_manage', '角色权限', 'MENU', 0, '/role', 'Lock', 7, 1),

-- 系统管理子菜单
('system:user', '用户管理', 'MENU', 1, '/system/user', 'User', 1, 1),
('system:user:add', '新增用户', 'BUTTON', 8, '', '', 1, 1),
('system:user:edit', '编辑用户', 'BUTTON', 8, '', '', 2, 1),
('system:user:delete', '删除用户', 'BUTTON', 8, '', '', 3, 1),
('system:role', '角色管理', 'MENU', 1, '/system/role', 'Role', 2, 1),
('system:role:add', '新增角色', 'BUTTON', 11, '', '', 1, 1),
('system:role:edit', '编辑角色', 'BUTTON', 11, '', '', 2, 1),
('system:role:delete', '删除角色', 'BUTTON', 11, '', '', 3, 1),
('system:role:assign', '分配权限', 'BUTTON', 11, '', '', 4, 1),
('system:permission', '权限管理', 'MENU', 1, '/system/permission', 'Permission', 3, 1),

-- 商品管理子菜单
('product:list', '商品列表', 'MENU', 2, '/product/list', 'List', 1, 1),
('product:add', '新增商品', 'BUTTON', 16, '', '', 1, 1),
('product:edit', '编辑商品', 'BUTTON', 16, '', '', 2, 1),
('product:delete', '删除商品', 'BUTTON', 16, '', '', 3, 1),
('product:sku', 'SKU 管理', 'MENU', 2, '/product/sku', 'Sku', 2, 1),
('product:sku:add', '新增 SKU', 'BUTTON', 19, '', '', 1, 1),
('product:sku:edit', '编辑 SKU', 'BUTTON', 19, '', '', 2, 1),
('product:sku:delete', '删除 SKU', 'BUTTON', 19, '', '', 3, 1),

-- 订单管理子菜单
('order:list', '订单列表', 'MENU', 3, '/order/list', 'Order', 1, 1),
('order:detail', '订单详情', 'BUTTON', 22, '', '', 1, 1),
('order:ship', '订单发货', 'BUTTON', 22, '', '', 2, 1),
('order:cancel', '取消订单', 'BUTTON', 22, '', '', 3, 1),

-- 库存管理子菜单
('inventory:list', '库存列表', 'MENU', 4, '/inventory/list', 'Inventory', 1, 1),
('inventory:adjust', '库存调整', 'BUTTON', 26, '', '', 1, 1),
('inventory:warning', '库存预警', 'MENU', 4, '/inventory/warning', 'Warning', 2, 1),

-- 报价管理子菜单
('quotation:list', '报价列表', 'MENU', 5, '/quotation/list', 'Quotation', 1, 1),
('quotation:audit', '报价审核', 'BUTTON', 29, '', '', 1, 1),
('quotation:counter', '还价', 'BUTTON', 29, '', '', 2, 1);

-- 给超级管理员分配所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM `permission`;

-- 初始化默认仓库
INSERT INTO `warehouse` (`name`, `code`, `address`, `status`) VALUES
('主仓库', 'WH001', '默认仓库地址', 1);

-- 初始化系统配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `description`, `is_public`) VALUES
('min_quotation_amount', '1.00', 'NUMBER', '最小报价金额', 0),
('max_quotation_amount', '1000000.00', 'NUMBER', '最大报价金额', 0),
('max_counter_offer_times', '5', 'NUMBER', '最大还价次数', 1),
('quotation_expire_hours', '72', 'NUMBER', '报价单过期时间 (小时)', 1),
('order_auto_cancel_minutes', '30', 'NUMBER', '订单自动取消时间 (分钟)', 0),
('system_maintenance', 'false', 'BOOLEAN', '系统维护模式', 1);
