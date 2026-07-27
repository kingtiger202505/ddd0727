<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        v-model="keyword" 
        placeholder="搜索商品" 
        @confirm="searchProducts"
      />
      <button class="btn-search" @click="searchProducts">搜索</button>
    </view>

    <!-- 分类筛选 -->
    <scroll-view scroll-x class="category-scroll">
      <view class="category-list">
        <view 
          :class="['category-item', currentCategory === 0 ? 'active' : '']"
          @click="selectCategory(0)"
        >全部</view>
        <view 
          v-for="(cat, index) in categories" 
          :key="index"
          :class="['category-item', currentCategory === cat.id ? 'active' : '']"
          @click="selectCategory(cat.id)"
        >{{ cat.name }}</view>
      </view>
    </scroll-view>

    <!-- 商品列表 -->
    <view class="product-section">
      <view 
        class="product-item" 
        v-for="product in products" 
        :key="product.id"
        @click="goToDetail(product.id)"
      >
        <image :src="product.mainImage || '/static/default-product.png'" mode="aspectFill" class="product-image"></image>
        <view class="product-info">
          <text class="product-name">{{ product.name }}</text>
          <text class="product-desc">{{ product.description }}</text>
          <view class="price-row">
            <text class="product-price">¥{{ product.price }}</text>
            <text class="product-stock">库存：{{ product.stock }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="products.length === 0" class="empty-state">
      <text>暂无商品</text>
    </view>
  </view>
</template>

<script>
import { productApi } from '@/utils/api.js';

export default {
  data() {
    return {
      keyword: '',
      currentCategory: 0,
      categories: [
        { id: 1, name: '电子产品' },
        { id: 2, name: '服装服饰' },
        { id: 3, name: '家居用品' },
        { id: 4, name: '食品饮料' },
        { id: 5, name: '图书文具' }
      ],
      products: []
    };
  },
  onLoad() {
    this.loadProducts();
  },
  onPullDownRefresh() {
    this.loadProducts().finally(() => {
      uni.stopPullDownRefresh();
    });
  },
  methods: {
    // 加载商品列表
    async loadProducts() {
      try {
        const res = await productApi.getList();
        this.products = res || [];
      } catch (e) {
        console.error('加载商品失败', e);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    },
    // 搜索商品
    searchProducts() {
      // TODO: 实现搜索功能
      uni.showToast({
        title: '搜索功能开发中',
        icon: 'none'
      });
    },
    // 选择分类
    selectCategory(catId) {
      this.currentCategory = catId;
      // TODO: 根据分类筛选
      uni.showToast({
        title: '分类筛选开发中',
        icon: 'none'
      });
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
  min-height: 100vh;
  background: #f5f5f5;
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;

  .search-input {
    flex: 1;
    height: 70rpx;
    background: #f5f5f5;
    border-radius: 35rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
  }

  .btn-search {
    margin-left: 20rpx;
    padding: 0 30rpx;
    height: 70rpx;
    line-height: 70rpx;
    background: #007AFF;
    color: #fff;
    border-radius: 35rpx;
    font-size: 28rpx;
  }
}

.category-scroll {
  white-space: nowrap;
  background: #fff;
  padding: 20rpx 0;
}

.category-list {
  display: inline-flex;
  padding: 0 20rpx;

  .category-item {
    display: inline-block;
    padding: 10rpx 30rpx;
    margin-right: 20rpx;
    font-size: 28rpx;
    color: #666;
    background: #f5f5f5;
    border-radius: 30rpx;

    &.active {
      color: #fff;
      background: #007AFF;
    }
  }
}

.product-section {
  padding: 20rpx;

  .product-item {
    display: flex;
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;

    .product-image {
      width: 240rpx;
      height: 240rpx;
      flex-shrink: 0;
    }

    .product-info {
      flex: 1;
      padding: 20rpx;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .product-name {
        font-size: 30rpx;
        font-weight: bold;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .product-desc {
        font-size: 26rpx;
        color: #999;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .price-row {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .product-price {
          font-size: 34rpx;
          color: #ff4444;
          font-weight: bold;
        }

        .product-stock {
          font-size: 24rpx;
          color: #999;
        }
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
