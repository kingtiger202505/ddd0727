import request from '@/utils/request'

// 获取权限树形结构
export function getPermissionTree() {
  return request({
    url: '/permission/tree',
    method: 'get'
  })
}

// 获取所有权限列表
export function getPermissionList(params) {
  return request({
    url: '/permission/list',
    method: 'get',
    params
  })
}

// 创建权限
export function createPermission(data) {
  return request({
    url: '/permission',
    method: 'post',
    data
  })
}

// 更新权限
export function updatePermission(data) {
  return request({
    url: '/permission',
    method: 'put',
    data
  })
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/permission/${id}`,
    method: 'delete'
  })
}
