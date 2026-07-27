<template>
  <view class="container">
    <!-- 商品信息 -->
    <view class="product-section" v-if="product">
      <image :src="product.mainImage || '/static/default-product.png'" mode="aspectFill" class="product-image"></image>
      <view class="product-info">
        <text class="product-name">{{ product.name }}</text>
        <text class="product-price">¥{{ product.price }}</text>
      </view>
    </view>

    <!-- 购买数量 -->
    <view class="quantity-section">
      <text class="label">购买数量</text>
      <view class="quantity-control">
        <button class="btn-minus" @click="decreaseQuantity">-</button>
        <text class="quantity-value">{{ quantity }}</text>
        <button class="btn-plus" @click="increaseQuantity">+</button>
      </view>
    </view>

    <!-- 收货地址 -->
    <view class="address-section" @click="selectAddress">
      <text class="label">收货地址</text>
      <view class="address-info" v-if="selectedAddress">
        <text>{{ selectedAddress.receiverName }} {{ selectedAddress.phone }}</text>
        <text>{{ selectedAddress.detailAddress }}</text>
      </view>
      <text class="arrow" v-else>请选择收货地址 ></text>
    </view>

    <!-- 订单备注 -->
    <view class="remark-section">
      <text class="label">订单备注</text>
      <input 
        class="remark-input" 
        v-model="remark" 
        placeholder="选填：对本订单的说明"
      />
    </view>

    <!-- 金额明细 -->
    <view class="amount-section">
      <view class="amount-row">
        <text>商品总额</text>
        <text>¥{{ totalAmount }}</text>
      </view>
      <view class="amount-row" v-if="freightAmount > 0">
        <text>运费</text>
        <text>¥{{ freightAmount }}</text>
      </view>
      <view class="amount-row total">
        <text>实付金额</text>
        <text class="total-amount">¥{{ payAmount }}</text>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="action-bar">
      <view class="total-info">
        <text>合计：</text>
        <text class="total-amount">¥{{ payAmount }}</text>
      </view>
      <button class="btn-submit" @click="submitOrder">提交订单</button>
    </view>
  </view>
</template>

<script>
import { productApi, orderApi, inventoryApi } from '@/utils/api.js';

export default {
  data() {
    return {
      productId: null,
      product: null,
      quantity: 1,
      maxStock: 0,
      selectedAddress: null,
      remark: '',
      freightAmount: 0
    };
  },
  computed: {
    totalAmount() {
      if (!this.product) return '0.00';
      return (this.product.price * this.quantity).toFixed(2);
    },
    payAmount() {
      const total = parseFloat(this.totalAmount) + this.freightAmount;
      return total.toFixed(2);
    }
  },
  onLoad(options) {
    this.productId = options.productId;
    this.loadProductDetail();
    this.loadInventory();
  },
  methods: {
    // 加载商品详情
    async loadProductDetail() {
      try {
        const res = await productApi.getDetail(this.productId);
        this.product = res;
      } catch (e) {
        console.error('加载商品失败', e);
        uni.showToast({
          title: '商品不存在',
          icon: 'none'
        });
        setTimeout(() => {
          uni.navigateBack();
        }, 1500);
      }
    },
    // 加载库存
    async loadInventory() {
      try {
        const res = await inventoryApi.getByProduct(this.productId);
        this.maxStock = res ? res.availableQuantity : 0;
        if (this.maxStock <= 0) {
          uni.showToast({
            title: '库存不足',
            icon: 'none'
          });
        }
      } catch (e) {
        this.maxStock = 0;
      }
    },
    // 减少数量
    decreaseQuantity() {
      if (this.quantity > 1) {
        this.quantity--;
      }
    },
    // 增加数量
    increaseQuantity() {
      if (this.quantity < this.maxStock) {
        this.quantity++;
      } else {
        uni.showToast({
          title: '已达最大库存',
          icon: 'none'
        });
      }
    },
    // 选择地址
    selectAddress() {
      // TODO: 跳转到地址选择页
      uni.showToast({
        title: '地址功能开发中',
        icon: 'none'
      });
    },
    // 提交订单
    async submitOrder() {
      if (!this.selectedAddress) {
        uni.showToast({
          title: '请选择收货地址',
          icon: 'none'
        });
        return;
      }

      if (this.quantity <= 0 || this.quantity > this.maxStock) {
        uni.showToast({
          title: '数量无效',
          icon: 'none'
        });
        return;
      }

      try {
        const orderData = {
          productId: this.productId,
          quantity: this.quantity,
          remark: this.remark,
          shippingAddress: this.selectedAddress
        };

        const orderId = await orderApi.create(orderData);
        
        uni.showToast({
          title: '订单创建成功',
          icon: 'success'
        });

        // 跳转到订单详情页
        setTimeout(() => {
          uni.redirectTo({
            url: `/pages/order/detail?id=${orderId}`
          });
        }, 1500);
      } catch (e) {
        console.error('创建订单失败', e);
      }
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

.product-section {
  display: flex;
  padding: 30rpx;
  background: #fff;
  margin-bottom: 20rpx;

  .product-image {
    width: 180rpx;
    height: 180rpx;
    border-radius: 8rpx;
    margin-right: 20rpx;
  }

  .product-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .product-name {
      font-size: 30rpx;
      font-weight: bold;
    }

    .product-price {
      font-size: 34rpx;
      color: #ff4444;
      font-weight: bold;
    }
  }
}

.quantity-section,
.address-section,
.remark-section,
.amount-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .label {
    display: block;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 20rpx;
  }
}

.quantity-control {
  display: flex;
  align-items: center;

  button {
    width: 60rpx;
    height: 60rpx;
    border-radius: 50%;
    font-size: 32rpx;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .btn-minus {
    background: #f5f5f5;
    color: #666;
  }

  .btn-plus {
    background: #007AFF;
    color: #fff;
  }

  .quantity-value {
    margin: 0 40rpx;
    font-size: 32rpx;
    font-weight: bold;
  }
}

.address-section {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .address-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    font-size: 28rpx;
    color: #333;
  }

  .arrow {
    font-size: 28rpx;
    color: #999;
  }
}

.remark-input {
  width: 100%;
  height: 80rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
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

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 8rpx rgba(0,0,0,0.1);

  .total-info {
    font-size: 28rpx;
    color: #666;

    .total-amount {
      color: #ff4444;
      font-size: 36rpx;
      font-weight: bold;
    }
  }

  .btn-submit {
    padding: 20rpx 60rpx;
    background: #ff4444;
    color: #fff;
    border-radius: 50rpx;
    font-size: 32rpx;
  }
}
</style>
