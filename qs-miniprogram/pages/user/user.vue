<template>
  <view class="container">
    <!-- 用户信息 -->
    <view class="user-info" v-if="isLoggedIn">
      <image :src="userInfo.avatar || '/static/default-avatar.png'" class="avatar"></image>
      <view class="user-detail">
        <text class="username">{{ userInfo.username }}</text>
        <text class="phone">{{ userInfo.phone }}</text>
      </view>
    </view>
    
    <!-- 登录按钮 -->
    <view class="login-section" v-else>
      <button class="btn-login" @click="goToLogin">立即登录</button>
    </view>
    
    <!-- 功能菜单 -->
    <view class="menu-list">
      <view class="menu-item" @click="goToPage('/pages/order/list')">
        <text class="menu-icon">📦</text>
        <text class="menu-text">我的订单</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/product/list')">
        <text class="menu-icon">🛍️</text>
        <text class="menu-text">商品管理</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/cart/cart')">
        <text class="menu-icon">🛒</text>
        <text class="menu-text">购物车</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/user/address')">
        <text class="menu-icon">📍</text>
        <text class="menu-text">收货地址</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>
    
    <!-- 退出登录 -->
    <view class="logout-section" v-if="isLoggedIn">
      <button class="btn-logout" @click="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
import { userApi } from '@/utils/api.js';
import http from '@/utils/request.js';

export default {
  data() {
    return {
      isLoggedIn: false,
      userInfo: {}
    };
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    // 检查登录状态
    async checkLoginStatus() {
      const token = http.getToken();
      if (token) {
        try {
          const res = await userApi.getCurrentUser();
          this.isLoggedIn = true;
          this.userInfo = res;
        } catch (e) {
          this.isLoggedIn = false;
          this.userInfo = {};
        }
      } else {
        this.isLoggedIn = false;
      }
    },
    // 跳转登录
    goToLogin() {
      uni.navigateTo({
        url: '/pages/user/login'
      });
    },
    // 跳转页面
    goToPage(url) {
      if (!this.isLoggedIn) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        });
        return;
      }
      uni.navigateTo({ url });
    },
    // 退出登录
    async logout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await userApi.logout();
            } catch (e) {
              console.error('登出失败', e);
            } finally {
              http.clearToken();
              this.isLoggedIn = false;
              this.userInfo = {};
              uni.reLaunch({ url: '/pages/index/index' });
            }
          }
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 40rpx 30rpx;
  background: #fff;
  
  .avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    margin-right: 30rpx;
  }
  
  .user-detail {
    display: flex;
    flex-direction: column;
    
    .username {
      font-size: 36rpx;
      font-weight: bold;
      margin-bottom: 10rpx;
    }
    
    .phone {
      font-size: 28rpx;
      color: #666;
    }
  }
}

.login-section {
  padding: 60rpx 30rpx;
  background: #fff;
  
  .btn-login {
    width: 100%;
    background: #007AFF;
    color: #fff;
    border-radius: 50rpx;
    font-size: 32rpx;
  }
}

.menu-list {
  margin-top: 20rpx;
  background: #fff;
  
  .menu-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #eee;
    
    .menu-icon {
      font-size: 40rpx;
      margin-right: 20rpx;
    }
    
    .menu-text {
      flex: 1;
      font-size: 30rpx;
    }
    
    .menu-arrow {
      font-size: 40rpx;
      color: #ccc;
    }
  }
}

.logout-section {
  margin-top: 40rpx;
  padding: 0 30rpx 40rpx;
  
  .btn-logout {
    width: 100%;
    background: #f44336;
    color: #fff;
    border-radius: 50rpx;
    font-size: 32rpx;
  }
}
</style>
