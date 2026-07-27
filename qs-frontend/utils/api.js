const BASE_URL = 'http://localhost:8080/api';

// 封装请求
export const request = (url, method = 'GET', data = {}) => {
  const token = uni.getStorageSync('token') || '';
  
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.code === 200) {
          resolve(res.data.data);
        } else if (res.statusCode === 401) {
          uni.removeStorageSync('token');
          uni.reLaunch({ url: '/pages/login/login' });
          reject(new Error('未登录'));
        } else {
          uni.showToast({ title: res.data.msg || '请求失败', icon: 'none' });
          reject(new Error(res.data.msg));
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络错误', icon: 'none' });
        reject(err);
      }
    });
  });
};

// API 方法
export const api = {
  // 获取报价列表
  getQuotationList: (userId, status) => request(`/quotation/list?userId=${userId}&status=${status || ''}`),
  
  // 获取详情
  getQuotationDetail: (id) => request(`/quotation/${id}`),
  
  // 创建报价
  createQuotation: (data) => request('/quotation/create', 'POST', data),
  
  // 还价
  counterOffer: (quotationId, buyerId, newPrice) => 
    request(`/quotation/counter-offer?quotationId=${quotationId}&buyerId=${buyerId}&newPrice=${newPrice}`, 'POST'),
  
  // 接受报价
  acceptQuotation: (quotationId, sellerId) => 
    request(`/quotation/accept?quotationId=${quotationId}&sellerId=${sellerId}`, 'POST'),
  
  // 拒绝报价
  rejectQuotation: (quotationId, sellerId) => 
    request(`/quotation/reject?quotationId=${quotationId}&sellerId=${sellerId}`, 'POST')
};
