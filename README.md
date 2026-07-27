# 电商报价议价系统 - 部署说明

## 项目架构

```
qs-ecommerce/
├── qs-domain/           # 领域层 (DDD)
├── qs-application/      # 应用层
├── qs-infrastructure/   # 基础设施层
├── qs-interfaces/       # 接口层
└── qs-miniprogram/      # UniApp 小程序端
```

## 技术栈

### 后端
- Spring Boot 3.x
- MyBatis-Plus (无 JPA，无悲观锁)
- Sa-Token (认证授权)
- MySQL 8.0+
- Lombok

### 前端
- UniApp (Vue3)
- 支持微信小程序和 H5

## 快速开始

### 1. 数据库初始化

```bash
mysql -u root -p < qs-infrastructure/src/main/resources/db/schema.sql
```

### 2. 配置数据库连接

编辑 `qs-interfaces/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/quotation_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 3. 启动后端服务

```bash
cd qs-interfaces
mvn spring-boot:run
```

服务将运行在 http://localhost:8080

### 4. 启动小程序

```bash
cd qs-miniprogram
npm install
# H5 开发
npm run dev:h5
# 或微信小程序开发
npm run dev:mp-weixin
```

## API 接口文档

### 用户接口
- POST /api/users/register - 用户注册
- POST /api/users/login - 用户登录
- POST /api/users/logout - 用户登出
- GET /api/users/current - 获取当前用户

### 商品接口
- GET /api/products/list - 获取商品列表
- GET /api/products/detail/{id} - 获取商品详情
- POST /api/products/create - 创建商品
- PUT /api/products/update - 更新商品
- DELETE /api/products/delete/{id} - 删除商品

### 订单接口
- GET /api/orders/my-orders - 获取我的订单
- GET /api/orders/detail/{id} - 获取订单详情
- POST /api/orders/create - 创建订单
- POST /api/orders/pay - 支付订单
- POST /api/orders/ship - 发货
- POST /api/orders/complete/{id} - 确认收货
- POST /api/orders/cancel - 取消订单

### 库存接口
- GET /api/inventory/list - 获取库存列表
- GET /api/inventory/product/{productId} - 根据商品获取库存
- POST /api/inventory/add/{id} - 增加库存
- POST /api/inventory/lock/{id} - 锁定库存
- POST /api/inventory/deduct/{id} - 扣减库存

## 测试账号

| 用户名 | 密码 | 角色 | 手机号 |
|--------|------|------|--------|
| admin | 123456 | ADMIN | 13800138000 |
| seller1 | 123456 | SELLER | 13800138001 |
| buyer1 | 123456 | BUYER | 13800138002 |

## 注意事项

1. 首次运行需要执行 schema.sql 创建数据库和表
2. 修改 application.yml 中的数据库配置
3. 小程序需要配置正确的后端 API 地址
4. 生产环境请修改默认密码和配置
