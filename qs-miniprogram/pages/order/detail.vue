<template>
  <view class="container">
    <!-- 订单头部信息 -->
    <view class="order-header">
      <text class="order-no">订单号：{{ order.orderNo }}</text>
      <text :class="['order-status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</text>
    </view>

    <!-- 商品信息列表 -->
    <view class="order-items-section">
      <view class="section-title">商品信息</view>
      <view 
        class="order-item" 
        v-for="item in order.items" 
        :key="item.id"
      >
        <image :src="item.productImage || '/static/default-product.png'" mode="aspectFill" class="item-image"></image>
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <view class="item-row">
            <text class="item-price">¥{{ item.price }}</text>
            <text class="item-quantity">x{{ item.quantity }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 收货地址 -->
    <view class="address-section" v-if="order.shippingAddress">
      <view class="section-title">收货地址</view>
      <view class="address-info">
        <text>{{ order.shippingAddress.receiverName }} {{ order.shippingAddress.phone }}</text>
        <text>{{ order.shippingAddress.province }}{{ order.shippingAddress.city }}{{ order.shippingAddress.district }}</text>
        <text>{{ order.shippingAddress.detailAddress }}</text>
      </view>
    </view>

    <!-- 订单金额 -->
    <view class="amount-section">
      <view class="amount-row">
        <text>商品总额</text>
        <text>¥{{ order.totalAmount }}</text>
      </view>
      <view class="amount-row" v-if="order.freightAmount > 0">
        <text>运费</text>
        <text>¥{{ order.freightAmount }}</text>
      </view>
      <view class="amount-row total">
        <text>实付金额</text>
        <text class="total-amount">¥{{ order.payAmount || order.totalAmount }}</text>
      </view>
    </view>

    <!-- 订单备注 -->
    <view class="remark-section" v-if="order.remark">
      <view class="section-title">订单备注</view>
      <text class="remark-text">{{ order.remark }}</text>
    </view>

    <!-- 物流信息 -->
    <view class="shipping-section" v-if="order.trackingNo">
      <view class="section-title">物流信息</view>
      <view class="shipping-info">
        <text>物流单号：{{ order.trackingNo }}</text>
        <text v-if="order.shippingTime">发货时间：{{ formatTime(order.shippingTime) }}</text>
      </view>
    </view>

    <!-- 时间信息 -->
    <view class="time-section">
      <view class="time-row">
        <text>创建时间</text>
        <text>{{ formatTime(order.createdAt) }}</text>
      </view>
      <view class="time-row" v-if="order.paymentTime">
        <text>支付时间</text>
        <text>{{ formatTime(order.paymentTime) }}</text>
      </view>
      <view class="time-row" v-if="order.receiveTime">
        <text>收货时间</text>
        <text>{{ formatTime(order.receiveTime) }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-bar" v-if="showActions">
      <button 
        v-if="order.status === 'PENDING_PAYMENT'" 
        class="btn-cancel" 
        @click="cancelOrder"
      >取消订单</button>
      <button 
        v-if="order.status === 'PENDING_PAYMENT'" 
        class="btn-pay" 
        @click="payOrder"
      >立即支付</button>
      <button 
        v-if="order.status === 'PAID' || order.status === 'SHIPPED'" 
        class="btn-receive" 
        @click="confirmReceive"
      >确认收货</button>
    </view>
  </view>
</template>

<script>
import { orderApi } from '@/utils/api.js';

export default {
  data() {
    return {
      orderId: null,
      order: {}
    };
  },
  computed: {
    showActions() {
      const status = this.order.status;
      return status === 'PENDING_PAYMENT' || status === 'PAID' || status === 'SHIPPED';
    }
  },
  onLoad(options) {
    this.orderId = options.id;
    this.loadOrderDetail();
  },
  methods: {
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await orderApi.getDetail(this.orderId);
        this.order = res;
      } catch (e) {
        console.error('加载订单详情失败', e);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    },
    // 支付订单
    async payOrder() {
      uni.showModal({
        title: '支付订单',
        content: '确认支付该订单？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.pay({ orderId: this.orderId });
              uni.showToast({
                title: '支付成功',
                icon: 'success'
              });
              this.loadOrderDetail();
            } catch (e) {
              console.error('支付失败', e);
            }
          }
        }
      });
    },
    // 确认收货
    async confirmReceive() {
      uni.showModal({
        title: '确认收货',
        content: '确认已收到商品？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.complete(this.orderId);
              uni.showToast({
                title: '确认成功',
                icon: 'success'
              });
              this.loadOrderDetail();
            } catch (e) {
              console.error('确认收货失败', e);
            }
          }
        }
      });
    },
    // 取消订单
    async cancelOrder() {
      uni.showModal({
        title: '取消订单',
        content: '确定要取消该订单？',
        editable: true,
        placeholderText: '请输入取消原因',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.cancel({ 
                orderId: this.orderId, 
                reason: res.content || '用户取消' 
              });
              uni.showToast({
                title: '已取消',
                icon: 'success'
              });
              this.loadOrderDetail();
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
    },
    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return '';
      const date = new Date(timeStr);
      return date.toLocaleString('zh-CN');
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  margin-bottom: 20rpx;

  .order-no {
    font-size: 28rpx;
    color: #666;
  }

  .order-status {
    font-size: 30rpx;
    font-weight: bold;

    &.status-pending { color: #ff9800; }
    &.status-paid { color: #2196f3; }
    &.status-shipped { color: #9c27b0; }
    &.status-completed { color: #4caf50; }
    &.status-cancelled { color: #f44336; }
  }
}

.order-items-section,
.address-section,
.amount-section,
.remark-section,
.shipping-section,
.time-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #eee;
  }
}

.order-item {
  display: flex;
  margin-bottom: 20rpx;

  .item-image {
    width: 160rpx;
    height: 160rpx;
    border-radius: 8rpx;
    margin-right: 20rpx;
  }

  .item-info {
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

.address-info,
.shipping-info {
  display: flex;
  flex-direction: column;
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.amount-row {
  display: flex;
  justify-content: space-between;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 15rpx;

  &.total {
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #eee;
    font-size: 30rpx;
    font-weight: bold;

    .total-amount {
      color: #ff4444;
      font-size: 36rpx;
    }
  }
}

.remark-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.time-row {
  display: flex;
  justify-content: space-between;
  font-size: 26rpx;
  color: #999;
  margin-bottom: 15rpx;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-end;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 8rpx rgba(0,0,0,0.1);

  button {
    margin-left: 20rpx;
    padding: 15rpx 40rpx;
    border-radius: 50rpx;
    font-size: 30rpx;
  }

  .btn-cancel {
    background: #f44336;
    color: #fff;
  }

  .btn-pay {
    background: #ff9800;
    color: #fff;
  }

  .btn-receive {
    background: #4caf50;
    color: #fff;
  }
}
</style>
