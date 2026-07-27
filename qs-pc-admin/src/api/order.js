import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function getOrderDetail(id) {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export function updateOrderStatus(id, status) {
  return request({
    url: `/order/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function getQuotationList(params) {
  return request({
    url: '/quotation/list',
    method: 'get',
    params
  })
}

export function approveQuotation(id, approved, counterPrice) {
  return request({
    url: `/quotation/${id}/approve`,
    method: 'post',
    data: { approved, counterPrice }
  })
}
