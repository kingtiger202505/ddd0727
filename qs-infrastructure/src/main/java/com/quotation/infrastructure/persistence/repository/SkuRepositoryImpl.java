package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.product.model.Sku;
import com.quotation.domain.product.repository.SkuRepository;
import com.quotation.infrastructure.persistence.mapper.SkuMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * SKU 仓储实现类
 */
@Repository
public class SkuRepositoryImpl implements SkuRepository {

    @Resource
    private SkuMapper skuMapper;

    @Override
    public Optional<Sku> findById(Long id) {
        Sku sku = skuMapper.selectById(id);
        return Optional.ofNullable(sku);
    }

    @Override
    public Optional<Sku> findBySkuCode(String skuCode) {
        Sku sku = skuMapper.selectBySkuCode(skuCode);
        return Optional.ofNullable(sku);
    }

    @Override
    public List<Sku> findByProductId(Long productId) {
        return skuMapper.selectByProductId(productId);
    }

    @Override
    public List<Sku> findAll() {
        return skuMapper.selectList(new LambdaQueryWrapper<Sku>().eq(Sku::getStatus, 1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Sku save(Sku sku) {
        if (sku.getId() == null) {
            skuMapper.insert(sku);
        } else {
            skuMapper.updateById(sku);
        }
        return sku;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        skuMapper.deleteById(id);
    }

    @Override
    public boolean existsBySkuCode(String skuCode) {
        Sku sku = skuMapper.selectBySkuCode(skuCode);
        return sku != null;
    }

    @Override
    public List<Sku> findByIds(List<Long> ids) {
        return skuMapper.selectBatchIds(ids);
    }

    /**
     * 批量保存 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<Sku> skus) {
        if (skus != null && !skus.isEmpty()) {
            skuMapper.batchInsert(skus);
        }
    }

    /**
     * 更新库存（乐观锁）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStockWithOptimisticLock(Long id, Integer newStock, Integer newLockStock) {
        Sku oldSku = skuMapper.selectById(id);
        if (oldSku == null) {
            return false;
        }
        
        int rows = skuMapper.updateStock(id, newStock, newLockStock, 
                                         oldSku.getStock(), oldSku.getLockStock());
        return rows > 0;
    }
}
