/**
 * API 请求封装
 */
const BASE_URL = 'http://localhost:8080/api';

// 获取 token
const getToken = () => {
  return uni.getStorageSync('token') || '';
};

// 设置 token
const setToken = (token) => {
  uni.setStorageSync('token', token);
};

// 清除 token
const clearToken = () => {
  uni.removeStorageSync('token');
};

/**
 * 请求封装
 */
const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': getToken() ? `Bearer ${getToken()}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data.data);
          } else if (res.data.code === 401) {
            // 未登录或 token 过期
            clearToken();
            uni.navigateTo({ url: '/pages/user/login' });
            reject(new Error(res.data.msg || '请先登录'));
          } else {
            uni.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none'
            });
            reject(new Error(res.data.msg));
          }
        } else {
          uni.showToast({
            title: '网络错误',
            icon: 'none'
          });
          reject(new Error('网络错误'));
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络异常',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

/**
 * GET 请求
 */
const get = (url, data) => {
  return request({ url, method: 'GET', data });
};

/**
 * POST 请求
 */
const post = (url, data) => {
  return request({ url, method: 'POST', data });
};

/**
 * PUT 请求
 */
const put = (url, data) => {
  return request({ url, method: 'PUT', data });
};

/**
 * DELETE 请求
 */
const del = (url, data) => {
  return request({ url, method: 'DELETE', data });
};

export default {
  BASE_URL,
  getToken,
  setToken,
  clearToken,
  request,
  get,
  post,
  put,
  del
};
