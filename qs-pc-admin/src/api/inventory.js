import request from '@/utils/request';

// 库存调整
export function adjustStock(data) {
  return request({
    url: '/api/inventory/adjust',
    method: 'post',
    data
  });
}

// 获取库存列表
export function getList(params) {
  return request({
    url: '/api/inventory/list',
    method: 'get',
    params
  });
}

// 获取 SKU 库存详情
export function getBySkuId(skuId) {
  return request({
    url: `/api/inventory/sku/${skuId}`,
    method: 'get'
  });
}

// 库存预警列表
export function getWarningList(params) {
  return request({
    url: '/api/inventory/warning',
    method: 'get',
    params
  });
}
