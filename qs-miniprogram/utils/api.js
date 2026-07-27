/**
 * API 接口管理
 */
import http from './request.js';

// 用户相关接口
export const userApi = {
  // 登录
  login: (data) => http.post('/users/login', data),
  // 注册
  register: (data) => http.post('/users/register', data),
  // 登出
  logout: () => http.post('/users/logout'),
  // 获取当前用户信息
  getCurrentUser: () => http.get('/users/current'),
  // 获取用户详情
  getUserById: (id) => http.get(`/users/${id}`)
};

// 商品相关接口
export const productApi = {
  // 获取商品列表（上架商品）
  getList: () => http.get('/products/list'),
  // 获取商品详情
  getDetail: (id) => http.get(`/products/detail/${id}`),
  // 获取我的商品
  getMyList: () => http.get('/products/my-list'),
  // 创建商品
  create: (data) => http.post('/products/create', data),
  // 更新商品
  update: (data) => http.put('/products/update', data),
  // 删除商品
  delete: (id) => http.del(`/products/delete/${id}`),
  // 上架商品
  enable: (id) => http.post(`/products/enable/${id}`),
  // 下架商品
  disable: (id) => http.post(`/products/disable/${id}`)
};

// 订单相关接口
export const orderApi = {
  // 获取我的订单
  getMyOrders: () => http.get('/orders/my-orders'),
  // 获取卖家订单
  getSellerOrders: () => http.get('/orders/seller-orders'),
  // 获取订单详情
  getDetail: (id) => http.get(`/orders/detail/${id}`),
  // 根据订单号获取
  getByOrderNo: (orderNo) => http.get(`/orders/order-no/${orderNo}`),
  // 创建订单
  create: (data) => http.post('/orders/create', data),
  // 支付订单
  pay: (data) => http.post('/orders/pay', data),
  // 发货
  ship: (data) => http.post('/orders/ship', data),
  // 确认收货
  complete: (orderId) => http.post(`/orders/complete/${orderId}`),
  // 取消订单
  cancel: (data) => http.post('/orders/cancel', data)
};

// 库存相关接口
export const inventoryApi = {
  // 获取库存列表
  getList: () => http.get('/inventory/list'),
  // 获取库存详情
  getDetail: (id) => http.get(`/inventory/detail/${id}`),
  // 根据商品 ID 获取库存
  getByProduct: (productId) => http.get(`/inventory/product/${productId}`),
  // 获取低库存预警
  getLowStock: () => http.get('/inventory/low-stock'),
  // 创建库存
  create: (data) => http.post('/inventory/create', data),
  // 更新库存
  update: (data) => http.put('/inventory/update', data),
  // 增加库存
  addStock: (inventoryId, quantity) => http.post(`/inventory/add/${inventoryId}`, { quantity }),
  // 锁定库存
  lockStock: (inventoryId, quantity) => http.post(`/inventory/lock/${inventoryId}`, { quantity }),
  // 释放库存
  releaseStock: (inventoryId, quantity) => http.post(`/inventory/release/${inventoryId}`, { quantity }),
  // 扣减库存
  deductStock: (inventoryId, quantity) => http.post(`/inventory/deduct/${inventoryId}`, { quantity })
};

// 报价相关接口
export const quotationApi = {
  // 获取报价列表
  getList: (status) => http.get('/quotations/list', { status }),
  // 获取报价详情
  getDetail: (id) => http.get(`/quotations/detail/${id}`),
  // 创建报价
  create: (data) => http.post('/quotations/create', data),
  // 更新报价
  update: (data) => http.put('/quotations/update', data),
  // 批准报价
  approve: (id) => http.post(`/quotations/approve/${id}`),
  // 拒绝报价
  reject: (id) => http.post(`/quotations/reject/${id}`)
};
