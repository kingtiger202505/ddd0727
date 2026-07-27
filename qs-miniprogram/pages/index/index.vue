<template>
  <view class="container">
    <!-- 顶部轮播 -->
    <swiper class="banner" indicator-dots autoplay circular>
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image :src="item.image" mode="aspectFill" class="banner-image"></image>
      </swiper-item>
    </swiper>

    <!-- 分类导航 -->
    <view class="category">
      <view class="category-item" v-for="(cat, index) in categories" :key="index">
        <image :src="cat.icon" class="category-icon"></image>
        <text>{{ cat.name }}</text>
      </view>
    </view>

    <!-- 商品列表 -->
    <view class="product-section">
      <view class="section-title">推荐商品</view>
      <view class="product-list">
        <view 
          class="product-item" 
          v-for="product in products" 
          :key="product.id"
          @click="goToDetail(product.id)"
        >
          <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-price">¥{{ product.price }}</text>
            <text class="product-stock">库存：{{ product.stock }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { productApi } from '@/utils/api.js';

export default {
  data() {
    return {
      banners: [
        { image: '/static/banner1.png' },
        { image: '/static/banner2.png' },
        { image: '/static/banner3.png' }
      ],
      categories: [
        { name: '全部', icon: '/static/category/all.png' },
        { name: '数码', icon: '/static/category/digital.png' },
        { name: '服装', icon: '/static/category/clothes.png' },
        { name: '食品', icon: '/static/category/food.png' },
        { name: '家居', icon: '/static/category/home.png' }
      ],
      products: []
    };
  },
  onLoad() {
    this.loadProducts();
  },
  methods: {
    // 加载商品列表
    async loadProducts() {
      try {
        const res = await productApi.getList();
        this.products = res || [];
      } catch (e) {
        console.error('加载商品失败', e);
      }
    },
    // 跳转商品详情
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/product/detail?id=${id}`
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  padding: 20rpx;
}

.banner {
  width: 100%;
  height: 300rpx;
  border-radius: 16rpx;
  overflow: hidden;
  
  .banner-image {
    width: 100%;
    height: 100%;
  }
}

.category {
  display: flex;
  justify-content: space-around;
  padding: 30rpx 0;
  
  .category-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .category-icon {
      width: 80rpx;
      height: 80rpx;
      margin-bottom: 10rpx;
    }
    
    text {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.product-section {
  margin-top: 30rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
  }
  
  .product-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    
    .product-item {
      width: 48%;
      background: #fff;
      border-radius: 16rpx;
      overflow: hidden;
      margin-bottom: 20rpx;
      box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
      
      .product-image {
        width: 100%;
        height: 300rpx;
      }
      
      .product-info {
        padding: 20rpx;
        display: flex;
        flex-direction: column;
        
        .product-name {
          font-size: 28rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-bottom: 10rpx;
        }
        
        .product-price {
          font-size: 32rpx;
          color: #ff4444;
          font-weight: bold;
        }
        
        .product-stock {
          font-size: 24rpx;
          color: #999;
          margin-top: 10rpx;
        }
      }
    }
  }
}
</style>
