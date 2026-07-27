<template>
  <view class="container">
    <!-- 购物车列表 -->
    <scroll-view scroll-y class="cart-list">
      <view 
        class="cart-item" 
        v-for="item in cartItems" 
        :key="item.id"
      >
        <checkbox 
          :checked="item.selected" 
          @change="toggleSelect(item)"
        />
        <image :src="item.productImage || '/static/default-product.png'" mode="aspectFill" class="item-image"></image>
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text class="item-price">¥{{ item.price }}</text>
          <view class="quantity-control">
            <button class="btn-minus" @click="decreaseQuantity(item)">-</button>
            <text class="quantity-value">{{ item.quantity }}</text>
            <button class="btn-plus" @click="increaseQuantity(item)">+</button>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 空状态 -->
    <view v-if="cartItems.length === 0" class="empty-state">
      <text>购物车是空的</text>
      <button class="btn-shop" @click="goToShop">去逛逛</button>
    </view>

    <!-- 底部操作栏 -->
    <view class="action-bar" v-if="cartItems.length > 0">
      <view class="select-all">
        <checkbox :checked="isAllSelected" @change="toggleAllSelect" />
        <text>全选</text>
      </view>
      <view class="total-info">
        <text>合计：</text>
        <text class="total-amount">¥{{ totalAmount }}</text>
      </view>
      <button class="btn-checkout" @click="checkout">结算 ({{ selectedCount }})</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      cartItems: []
    };
  },
  computed: {
    isAllSelected() {
      if (this.cartItems.length === 0) return false;
      return this.cartItems.every(item => item.selected);
    },
    selectedCount() {
      return this.cartItems.filter(item => item.selected).length;
    },
    totalAmount() {
      return this.cartItems
        .filter(item => item.selected)
        .reduce((sum, item) => sum + item.price * item.quantity, 0)
        .toFixed(2);
    }
  },
  onShow() {
    this.loadCart();
  },
  methods: {
    // 加载购物车
    async loadCart() {
      // TODO: 调用购物车 API
      // 暂时使用模拟数据
      this.cartItems = [
        {
          id: 1,
          productName: 'iPhone 15 Pro',
          productImage: '/static/product1.png',
          price: 7999.00,
          quantity: 1,
          selected: true
        },
        {
          id: 2,
          productName: 'AirPods Pro 2',
          productImage: '/static/product2.png',
          price: 1899.00,
          quantity: 2,
          selected: false
        }
      ];
    },
    // 切换单个商品选中状态
    toggleSelect(item) {
      item.selected = !item.selected;
    },
    // 切换全选
    toggleAllSelect() {
      const newValue = !this.isAllSelected;
      this.cartItems.forEach(item => {
        item.selected = newValue;
      });
    },
    // 减少数量
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--;
      } else {
        this.removeFromCart(item);
      }
    },
    // 增加数量
    increaseQuantity(item) {
      item.quantity++;
    },
    // 移除商品
    removeFromCart(item) {
      uni.showModal({
        title: '移除商品',
        content: '确定要移除该商品？',
        success: (res) => {
          if (res.confirm) {
            const index = this.cartItems.findIndex(i => i.id === item.id);
            if (index > -1) {
              this.cartItems.splice(index, 1);
            }
          }
        }
      });
    },
    // 结算
    checkout() {
      if (this.selectedCount === 0) {
        uni.showToast({
          title: '请选择商品',
          icon: 'none'
        });
        return;
      }
      
      // TODO: 跳转到订单确认页
      uni.showToast({
        title: '结算功能开发中',
        icon: 'none'
      });
    },
    // 去逛逛
    goToShop() {
      uni.switchTab({
        url: '/pages/index/index'
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

.cart-list {
  height: calc(100vh - 120rpx);
  padding: 20rpx;
}

.cart-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .item-image {
    width: 180rpx;
    height: 180rpx;
    border-radius: 8rpx;
    margin: 0 20rpx;
  }

  .item-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .item-name {
      font-size: 28rpx;
      font-weight: bold;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .item-price {
      font-size: 32rpx;
      color: #ff4444;
      font-weight: bold;
      margin-top: 10rpx;
    }

    .quantity-control {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      margin-top: 10rpx;

      button {
        width: 50rpx;
        height: 50rpx;
        border-radius: 50%;
        font-size: 28rpx;
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
        margin: 0 20rpx;
        font-size: 28rpx;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60vh;
  color: #999;

  text {
    font-size: 30rpx;
    margin-bottom: 30rpx;
  }

  .btn-shop {
    padding: 20rpx 60rpx;
    background: #007AFF;
    color: #fff;
    border-radius: 50rpx;
    font-size: 30rpx;
  }
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 8rpx rgba(0,0,0,0.1);

  .select-all {
    display: flex;
    align-items: center;
    font-size: 28rpx;
    color: #666;
  }

  .total-info {
    flex: 1;
    text-align: right;
    font-size: 28rpx;
    color: #666;

    .total-amount {
      color: #ff4444;
      font-size: 36rpx;
      font-weight: bold;
    }
  }

  .btn-checkout {
    margin-left: 20rpx;
    padding: 20rpx 50rpx;
    background: #ff4444;
    color: #fff;
    border-radius: 50rpx;
    font-size: 30rpx;
  }
}
</style>
