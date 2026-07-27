package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.product.model.Product;
import com.quotation.domain.product.repository.ProductRepository;
import com.quotation.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 商品仓储实现
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Resource
    private ProductMapper productMapper;

    @Override
    public void save(Product product) {
        productMapper.insert(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMapper.selectById(id));
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getSellerId, sellerId)
               .orderByDesc(Product::getCreatedAt);
        return productMapper.selectList(wrapper);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, categoryId)
               .eq(Product::getStatus, 1)
               .orderByDesc(Product::getCreatedAt);
        return productMapper.selectList(wrapper);
    }

    @Override
    public List<Product> findEnabledProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
               .orderByDesc(Product::getCreatedAt);
        return productMapper.selectList(wrapper);
    }

    @Override
    public void update(Product product) {
        productMapper.updateById(product);
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}
