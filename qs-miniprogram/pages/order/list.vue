<template>
  <view class="container">
    <!-- 订单列表 -->
    <scroll-view scroll-y class="order-list">
      <view 
        class="order-item" 
        v-for="order in orders" 
        :key="order.id"
        @click="goToDetail(order.id)"
      >
        <!-- 订单头部 -->
        <view class="order-header">
          <text class="order-no">订单号：{{ order.orderNo }}</text>
          <text :class="['order-status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</text>
        </view>
        
        <!-- 商品信息 -->
        <view class="order-items">
          <view class="order-item-info" v-for="item in order.items" :key="item.id">
            <image :src="item.productImage" mode="aspectFill" class="item-image"></image>
            <view class="item-detail">
              <text class="item-name">{{ item.productName }}</text>
              <view class="item-row">
                <text class="item-price">¥{{ item.price }}</text>
                <text class="item-quantity">x{{ item.quantity }}</text>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 订单底部 -->
        <view class="order-footer">
          <text class="order-total">合计：¥{{ order.totalAmount }}</text>
          <view class="order-actions">
            <button 
              v-if="order.status === 'PENDING_PAYMENT'" 
              class="btn-pay" 
              @click.stop="payOrder(order.id)"
            >去支付</button>
            <button 
              v-if="order.status === 'PAID'" 
              class="btn-receive" 
              @click.stop="confirmReceive(order.id)"
            >确认收货</button>
            <button 
              v-if="order.status === 'PENDING_PAYMENT'" 
              class="btn-cancel" 
              @click.stop="cancelOrder(order.id)"
            >取消订单</button>
          </view>
        </view>
      </view>
    </scroll-view>
    
    <!-- 空状态 -->
    <view v-if="orders.length === 0" class="empty-state">
      <text>暂无订单</text>
    </view>
  </view>
</template>

<script>
import { orderApi } from '@/utils/api.js';

export default {
  data() {
    return {
      orders: []
    };
  },
  onLoad() {
    this.loadOrders();
  },
  onShow() {
    this.loadOrders();
  },
  methods: {
    // 加载订单列表
    async loadOrders() {
      try {
        const res = await orderApi.getMyOrders();
        this.orders = res || [];
      } catch (e) {
        console.error('加载订单失败', e);
      }
    },
    // 跳转订单详情
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/order/detail?id=${id}`
      });
    },
    // 支付订单
    async payOrder(orderId) {
      try {
        await orderApi.pay({ orderId });
        uni.showToast({
          title: '支付成功',
          icon: 'success'
        });
        this.loadOrders();
      } catch (e) {
        console.error('支付失败', e);
      }
    },
    // 确认收货
    async confirmReceive(orderId) {
      uni.showModal({
        title: '确认收货',
        content: '确认已收到商品？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.complete(orderId);
              uni.showToast({
                title: '确认成功',
                icon: 'success'
              });
              this.loadOrders();
            } catch (e) {
              console.error('确认收货失败', e);
            }
          }
        }
      });
    },
    // 取消订单
    async cancelOrder(orderId) {
      uni.showModal({
        title: '取消订单',
        content: '确定要取消该订单？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.cancel({ orderId, reason: '用户取消' });
              uni.showToast({
                title: '已取消',
                icon: 'success'
              });
              this.loadOrders();
            } catch (e) {
              console.error('取消订单失败', e);
            }
          }
        }
      });
    },
    // 获取状态样式类
    getStatusClass(status) {
      const map = {
        'PENDING_PAYMENT': 'status-pending',
        'PAID': 'status-paid',
        'SHIPPED': 'status-shipped',
        'COMPLETED': 'status-completed',
        'CANCELLED': 'status-cancelled'
      };
      return map[status] || '';
    },
    // 获取状态文本
    getStatusText(status) {
      const map = {
        'PENDING_PAYMENT': '待支付',
        'PAID': '已支付',
        'SHIPPED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return map[status] || status;
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  padding: 20rpx;
  background: #f5f5f5;
  min-height: 100vh;
}

.order-list {
  height: 100vh;
}

.order-item {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  
  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx;
    border-bottom: 1rpx solid #eee;
    
    .order-no {
      font-size: 26rpx;
      color: #666;
    }
    
    .order-status {
      font-size: 28rpx;
      font-weight: bold;
      
      &.status-pending { color: #ff9800; }
      &.status-paid { color: #2196f3; }
      &.status-shipped { color: #9c27b0; }
      &.status-completed { color: #4caf50; }
      &.status-cancelled { color: #f44336; }
    }
  }
  
  .order-items {
    padding: 20rpx;
    
    .order-item-info {
      display: flex;
      margin-bottom: 20rpx;
      
      .item-image {
        width: 160rpx;
        height: 160rpx;
        border-radius: 8rpx;
        margin-right: 20rpx;
      }
      
      .item-detail {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        
        .item-name {
          font-size: 28rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        
        .item-row {
          display: flex;
          justify-content: space-between;
          
          .item-price {
            font-size: 30rpx;
            color: #ff4444;
            font-weight: bold;
          }
          
          .item-quantity {
            font-size: 26rpx;
            color: #999;
          }
        }
      }
    }
  }
  
  .order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx;
    border-top: 1rpx solid #eee;
    
    .order-total {
      font-size: 28rpx;
      color: #333;
      
      text {
        font-size: 32rpx;
        color: #ff4444;
        font-weight: bold;
      }
    }
    
    .order-actions {
      display: flex;
      
      button {
        margin-left: 20rpx;
        padding: 10rpx 30rpx;
        font-size: 26rpx;
        border-radius: 40rpx;
      }
      
      .btn-pay {
        background: #ff9800;
        color: #fff;
      }
      
      .btn-receive {
        background: #4caf50;
        color: #fff;
      }
      
      .btn-cancel {
        background: #f44336;
        color: #fff;
      }
    }
  }
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400rpx;
  color: #999;
  font-size: 28rpx;
}
</style>
