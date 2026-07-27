package com.quotation.domain.product.repository;

import com.quotation.domain.product.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * 商品仓储接口
 */
public interface ProductRepository {
    
    /**
     * 保存商品
     */
    void save(Product product);
    
    /**
     * 根据 ID 查找商品
     */
    Optional<Product> findById(Long id);
    
    /**
     * 根据卖家 ID 查询商品列表
     */
    List<Product> findBySellerId(Long sellerId);
    
    /**
     * 根据分类 ID 查询商品列表
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * 查询上架商品
     */
    List<Product> findEnabledProducts();
    
    /**
     * 更新商品信息
     */
    void update(Product product);
    
    /**
     * 删除商品
     */
    void delete(Long id);
}
