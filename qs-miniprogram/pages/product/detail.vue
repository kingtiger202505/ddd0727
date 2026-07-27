<template>
  <view class="container">
    <!-- 商品图片 -->
    <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
    
    <!-- 商品信息 -->
    <view class="product-info">
      <text class="product-name">{{ product.name }}</text>
      <text class="product-desc">{{ product.description }}</text>
      <view class="price-row">
        <text class="product-price">¥{{ product.price }}</text>
        <text class="product-stock">库存：{{ stock }}</text>
      </view>
    </view>
    
    <!-- 操作按钮 -->
    <view class="action-bar">
      <button class="btn-cart" @click="addToCart">加入购物车</button>
      <button class="btn-buy" @click="buyNow">立即购买</button>
    </view>
  </view>
</template>

<script>
import { productApi, inventoryApi } from '@/utils/api.js';

export default {
  data() {
    return {
      productId: null,
      product: {},
      stock: 0
    };
  },
  onLoad(options) {
    this.productId = options.id;
    this.loadProductDetail();
  },
  methods: {
    // 加载商品详情
    async loadProductDetail() {
      try {
        const res = await productApi.getDetail(this.productId);
        this.product = res;
        // 加载库存
        this.loadInventory();
      } catch (e) {
        console.error('加载商品详情失败', e);
      }
    },
    // 加载库存
    async loadInventory() {
      try {
        const res = await inventoryApi.getByProduct(this.productId);
        this.stock = res ? res.availableQuantity : 0;
      } catch (e) {
        this.stock = 0;
      }
    },
    // 加入购物车
    addToCart() {
      uni.showToast({
        title: '已加入购物车',
        icon: 'success'
      });
    },
    // 立即购买
    buyNow() {
      if (this.stock <= 0) {
        uni.showToast({
          title: '库存不足',
          icon: 'none'
        });
        return;
      }
      uni.navigateTo({
        url: `/pages/order/create?productId=${this.productId}`
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  padding-bottom: 120rpx;
}

.product-image {
  width: 100%;
  height: 700rpx;
}

.product-info {
  padding: 30rpx;
  background: #fff;
  
  .product-name {
    font-size: 36rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 20rpx;
  }
  
  .product-desc {
    font-size: 28rpx;
    color: #666;
    display: block;
    margin-bottom: 20rpx;
  }
  
  .price-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .product-price {
      font-size: 40rpx;
      color: #ff4444;
      font-weight: bold;
    }
    
    .product-stock {
      font-size: 28rpx;
      color: #999;
    }
  }
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 8rpx rgba(0,0,0,0.1);
  
  button {
    flex: 1;
    border-radius: 50rpx;
    font-size: 30rpx;
  }
  
  .btn-cart {
    background: #ffc107;
    color: #fff;
    margin-right: 20rpx;
  }
  
  .btn-buy {
    background: #ff4444;
    color: #fff;
  }
}
</style>
