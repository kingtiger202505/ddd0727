import request from '@/utils/request'

// 获取库存列表
export function getInventoryList(params) {
  return request({
    url: '/inventory/list',
    method: 'get',
    params
  })
}

// 库存调整
export function updateInventory(data) {
  return request({
    url: '/inventory',
    method: 'put',
    data
  })
}

// 获取 SKU 库存详情
export function getInventoryBySku(skuId) {
  return request({
    url: `/inventory/sku/${skuId}`,
    method: 'get'
  })
}

// 库存预警列表
export function getWarningList(params) {
  return request({
    url: '/inventory/warning',
    method: 'get',
    params
  })
}
