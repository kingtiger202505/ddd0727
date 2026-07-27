import request from '@/utils/request';

// 获取 SKU 列表
export function getList(params) {
  return request({
    url: '/api/sku/list',
    method: 'get',
    params
  });
}

// 创建 SKU
export function create(data) {
  return request({
    url: '/api/sku',
    method: 'post',
    data
  });
}

// 更新 SKU
export function update(data) {
  return request({
    url: '/api/sku',
    method: 'put',
    data
  });
}

// 删除 SKU
export function deleteSku(id) {
  return request({
    url: `/api/sku/${id}`,
    method: 'delete'
  });
}

// 获取 SKU 详情
export function getById(id) {
  return request({
    url: `/api/sku/${id}`,
    method: 'get'
  });
}
