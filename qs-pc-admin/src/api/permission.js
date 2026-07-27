import request from '@/utils/request';

// 获取权限树形结构
export function getTree() {
  return request({
    url: '/api/permission/tree',
    method: 'get'
  });
}

// 获取所有权限列表
export function getList(params) {
  return request({
    url: '/api/permission/list',
    method: 'get',
    params
  });
}

// 创建权限
export function create(data) {
  return request({
    url: '/api/permission',
    method: 'post',
    data
  });
}

// 更新权限
export function update(data) {
  return request({
    url: '/api/permission',
    method: 'put',
    data
  });
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/api/permission/${id}`,
    method: 'delete'
  });
}
