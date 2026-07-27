import request from '@/utils/request'

// 获取 SKU 列表
export function getSkuList(params) {
  return request({
    url: '/sku/list',
    method: 'get',
    params
  })
}

// 创建 SKU
export function createSku(data) {
  return request({
    url: '/sku',
    method: 'post',
    data
  })
}

// 更新 SKU
export function updateSku(data) {
  return request({
    url: '/sku',
    method: 'put',
    data
  })
}

// 删除 SKU
export function deleteSku(id) {
  return request({
    url: `/sku/${id}`,
    method: 'delete'
  })
}

// 获取 SKU 详情
export function getSkuById(id) {
  return request({
    url: `/sku/${id}`,
    method: 'get'
  })
}
