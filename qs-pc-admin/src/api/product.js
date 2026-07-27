import request from '@/utils/request'

export function getProductList(params) {
  return request({
    url: '/product/list',
    method: 'get',
    params
  })
}

export function getProductDetail(id) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

export function createProduct(data) {
  return request({
    url: '/product',
    method: 'post',
    data
  })
}

export function updateProduct(id, data) {
  return request({
    url: `/product/${id}`,
    method: 'put',
    data
  })
}

export function deleteProduct(id) {
  return request({
    url: `/product/${id}`,
    method: 'delete'
  })
}

export function getSkuList(params) {
  return request({
    url: '/sku/list',
    method: 'get',
    params
  })
}

export function createSku(data) {
  return request({
    url: '/sku',
    method: 'post',
    data
  })
}

export function updateSku(id, data) {
  return request({
    url: `/sku/${id}`,
    method: 'put',
    data
  })
}

export function deleteSku(id) {
  return request({
    url: `/sku/${id}`,
    method: 'delete'
  })
}
