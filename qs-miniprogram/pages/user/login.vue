<template>
  <view class="container">
    <view class="form-section">
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          v-model="formData.phone" 
          type="number"
          placeholder="请输入手机号"
        />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input 
          class="input" 
          v-model="formData.password" 
          type="password"
          placeholder="请输入密码"
        />
      </view>
    </view>
    
    <button class="btn-submit" @click="handleLogin">登录</button>
    
    <view class="register-link">
      <text>没有账号？</text>
      <text class="link" @click="goToRegister">立即注册</text>
    </view>
  </view>
</template>

<script>
import { userApi } from '@/utils/api.js';
import http from '@/utils/request.js';

export default {
  data() {
    return {
      formData: {
        phone: '',
        password: ''
      }
    };
  },
  methods: {
    // 登录
    async handleLogin() {
      const { phone, password } = this.formData;
      
      if (!phone || !password) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      
      try {
        const res = await userApi.login({
          phone,
          password
        });
        
        // 保存 token
        if (res && res.tokenValue) {
          http.setToken(res.tokenValue);
        }
        
        uni.showToast({
          title: '登录成功',
          icon: 'success'
        });
        
        // 跳转首页
        setTimeout(() => {
          uni.reLaunch({ url: '/pages/index/index' });
        }, 1500);
      } catch (e) {
        console.error('登录失败', e);
      }
    },
    // 跳转注册
    goToRegister() {
      uni.navigateTo({ url: '/pages/user/register' });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  padding: 40rpx;
}

.form-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  
  .form-item {
    margin-bottom: 30rpx;
    
    .label {
      display: block;
      font-size: 28rpx;
      color: #333;
      margin-bottom: 15rpx;
    }
    
    .input {
      width: 100%;
      height: 80rpx;
      background: #f5f5f5;
      border-radius: 10rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
    }
  }
}

.btn-submit {
  margin-top: 40rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 50rpx;
  font-size: 32rpx;
}

.register-link {
  margin-top: 40rpx;
  text-align: center;
  font-size: 28rpx;
  color: #666;
  
  .link {
    color: #007AFF;
    margin-left: 10rpx;
  }
}
</style>
