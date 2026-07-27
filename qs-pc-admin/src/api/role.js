import request from '@/utils/request';

// 获取角色列表
export function getList(params) {
  return request({
    url: '/api/role/list',
    method: 'get',
    params
  });
}

// 创建角色
export function create(data) {
  return request({
    url: '/api/role',
    method: 'post',
    data
  });
}

// 更新角色
export function update(data) {
  return request({
    url: '/api/role',
    method: 'put',
    data
  });
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/api/role/${id}`,
    method: 'delete'
  });
}

// 获取角色详情
export function getById(id) {
  return request({
    url: `/api/role/${id}`,
    method: 'get'
  });
}

// 获取角色的权限列表
export function getPermissions(roleId) {
  return request({
    url: `/api/role/${roleId}/permissions`,
    method: 'get'
  });
}

// 保存角色权限
export function savePermissions(data) {
  return request({
    url: '/api/role/permissions',
    method: 'post',
    data
  });
}
