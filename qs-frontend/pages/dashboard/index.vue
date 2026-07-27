<template>
  <view class="dashboard">
    <view class="header">数据看板</view>
    <view class="card-list">
      <view class="card">
        <text class="label">今日 GMV</text>
        <text class="value">¥{{ stats.gmv }}</text>
      </view>
      <view class="card">
        <text class="label">议价成功率</text>
        <text class="value">{{ stats.successRate }}%</text>
      </view>
      <view class="card">
        <text class="label">成交订单数</text>
        <text class="value">{{ stats.orderCount }}</text>
      </view>
      <view class="card">
        <text class="label">活跃用户</text>
        <text class="value">{{ stats.activeUsers }}</text>
      </view>
    </view>
    <view class="chart-container">
      <text class="chart-title">近 7 日交易趋势</text>
      <view class="chart-placeholder">图表区域 (集成 uCharts/ECharts)</view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getDashboardStats } from '@/utils/api';

const stats = ref({ gmv: 0, successRate: 0, orderCount: 0, activeUsers: 0 });

onMounted(async () => {
  const res = await getDashboardStats();
  if (res.code === 200) stats.value = res.data;
});
</script>

<style scoped>
.dashboard { padding: 20rpx; }
.header { font-size: 36rpx; font-weight: bold; margin-bottom: 30rpx; }
.card-list { display: flex; flex-wrap: wrap; justify-content: space-between; }
.card { width: 48%; background: #fff; padding: 30rpx; border-radius: 16rpx; margin-bottom: 20rpx; box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05); }
.label { font-size: 24rpx; color: #999; }
.value { font-size: 40rpx; font-weight: bold; color: #333; margin-top: 10rpx; display: block; }
.chart-container { background: #fff; padding: 30rpx; border-radius: 16rpx; margin-top: 30rpx; }
.chart-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; display: block; }
.chart-placeholder { height: 400rpx; background: #f5f5f5; display: flex; align-items: center; justify-content: center; color: #999; }
</style>
