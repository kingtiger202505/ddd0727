package com.quotation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quotation.domain.product.model.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SKU Mapper 接口
 */
@Mapper
public interface SkuMapper extends BaseMapper<Sku> {

    /**
     * 根据商品 ID 查询 SKU 列表
     */
    List<Sku> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据 SKU 编码查询
     */
    Sku selectBySkuCode(@Param("skuCode") String skuCode);

    /**
     * 批量插入 SKU
     */
    int batchInsert(@Param("list") List<Sku> skus);

    /**
     * 更新库存（乐观锁）
     */
    int updateStock(@Param("id") Long id, 
                    @Param("stock") Integer stock,
                    @Param("lockStock") Integer lockStock,
                    @Param("oldStock") Integer oldStock,
                    @Param("oldLockStock") Integer oldLockStock);
}
