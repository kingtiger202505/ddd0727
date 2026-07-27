<template>
  <view class="container">
    <view class="form-section">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          v-model="formData.username" 
          placeholder="请输入用户名"
        />
      </view>
      
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
      
      <view class="form-item">
        <text class="label">确认密码</text>
        <input 
          class="input" 
          v-model="formData.confirmPassword" 
          type="password"
          placeholder="请再次输入密码"
        />
      </view>
    </view>
    
    <button class="btn-submit" @click="handleRegister">注册</button>
    
    <view class="login-link">
      <text>已有账号？</text>
      <text class="link" @click="goToLogin">立即登录</text>
    </view>
  </view>
</template>

<script>
import { userApi } from '@/utils/api.js';

export default {
  data() {
    return {
      formData: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: ''
      }
    };
  },
  methods: {
    // 注册
    async handleRegister() {
      const { username, phone, password, confirmPassword } = this.formData;
      
      // 表单验证
      if (!username || !phone || !password) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      
      if (password !== confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        });
        return;
      }
      
      try {
        await userApi.register({
          username,
          phone,
          password,
          role: 'BUYER' // 默认买家角色
        });
        
        uni.showToast({
          title: '注册成功',
          icon: 'success'
        });
        
        // 跳转到登录页
        setTimeout(() => {
          uni.navigateTo({ url: '/pages/user/login' });
        }, 1500);
      } catch (e) {
        console.error('注册失败', e);
      }
    },
    // 跳转登录
    goToLogin() {
      uni.navigateTo({ url: '/pages/user/login' });
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

.login-link {
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
