import request from '@/utils/request'

// 获取角色列表
export function getRoleList(params) {
  return request({
    url: '/role/list',
    method: 'get',
    params
  })
}

// 创建角色
export function createRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(data) {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

// 获取角色详情
export function getRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

// 获取角色的权限列表
export function getRolePermissions(roleId) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'get'
  })
}

// 保存角色权限
export function saveRolePermissions(data) {
  return request({
    url: '/role/permissions',
    method: 'post',
    data
  })
}
