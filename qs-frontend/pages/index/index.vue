<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input type="text" placeholder="搜索报价单" v-model="keyword" />
    </view>

    <!-- 列表 -->
    <scroll-view scroll-y class="list-container">
      <view v-if="loading" class="loading">加载中...</view>
      <view v-else-if="quotations.length === 0" class="empty">暂无数据</view>
      <view v-else>
        <view 
          v-for="item in quotations" 
          :key="item.id" 
          class="quotation-item"
          @click="goDetail(item.id)"
        >
          <view class="item-header">
            <text class="item-id">单号：{{ item.id }}</text>
            <text :class="['status-tag', 'status-' + item.status.toLowerCase()]">
              {{ getStatusText(item.status) }}
            </text>
          </view>
          <view class="item-body">
            <text class="price">¥{{ (item.price / 100).toFixed(2) }}</text>
            <text class="time">{{ formatTime(item.createTime) }}</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 底部按钮 -->
    <view class="footer">
      <button class="create-btn" @click="goCreate">+ 创建报价</button>
    </view>
  </view>
</template>

<script>
import { api } from '@/utils/api.js';

export default {
  data() {
    return {
      quotations: [],
      loading: false,
      keyword: '',
      userId: 1001 // 模拟当前用户ID
    };
  },
  onLoad() {
    this.loadList();
  },
  onPullDownRefresh() {
    this.loadList().then(() => {
      uni.stopPullDownRefresh();
    });
  },
  methods: {
    async loadList() {
      this.loading = true;
      try {
        this.quotations = await api.getQuotationList(this.userId);
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },
    goDetail(id) {
      uni.navigateTo({ url: `/pages/detail/detail?id=${id}` });
    },
    goCreate() {
      uni.navigateTo({ url: '/pages/create/create' });
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待处理',
        'COUNTERED': '已还价',
        'ACCEPTED': '已接受',
        'REJECTED': '已拒绝'
      };
      return map[status] || status;
    },
    formatTime(timeStr) {
      if (!timeStr) return '';
      const date = new Date(timeStr);
      return `${date.getMonth()+1}/${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2,'0')}`;
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20rpx;
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.search-bar {
  margin-bottom: 20rpx;
}
.search-bar input {
  width: 100%;
  height: 70rpx;
  background: #f5f5f5;
  border-radius: 35rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
}
.list-container {
  flex: 1;
  overflow: hidden;
}
.quotation-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}
.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.item-id {
  font-size: 26rpx;
  color: #999;
}
.status-tag {
  font-size: 24rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}
.status-pending { background: #ffecdb; color: #ff9500; }
.status-countered { background: #e3f2fd; color: #2196f3; }
.status-accepted { background: #e8f5e9; color: #4caf50; }
.status-rejected { background: #ffebee; color: #f44336; }
.item-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff5252;
}
.time {
  font-size: 24rpx;
  color: #ccc;
}
.loading, .empty {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
}
.footer {
  margin-top: 20rpx;
}
.create-btn {
  background: linear-gradient(90deg, #ff6b6b, #ff5252);
  color: #fff;
  border: none;
  border-radius: 35rpx;
  font-size: 30rpx;
  padding: 20rpx 0;
}
</style>
