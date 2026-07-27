package com.quotation.domain.product.repository;

import com.quotation.domain.product.model.Sku;
import java.util.List;
import java.util.Optional;

/**
 * SKU 仓储接口
 */
public interface SkuRepository {
    
    Optional<Sku> findById(Long id);
    
    Optional<Sku> findBySkuCode(String skuCode);
    
    List<Sku> findByProductId(Long productId);
    
    List<Sku> findAll();
    
    Sku save(Sku sku);
    
    void deleteById(Long id);
    
    boolean existsBySkuCode(String skuCode);
    
    List<Sku> findByIds(List<Long> ids);
}
