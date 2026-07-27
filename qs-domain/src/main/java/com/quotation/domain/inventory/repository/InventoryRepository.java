package com.quotation.domain.inventory.repository;

import com.quotation.domain.inventory.model.Inventory;
import java.util.List;
import java.util.Optional;

/**
 * 库存仓储接口
 */
public interface InventoryRepository {
    
    /**
     * 保存库存记录
     */
    void save(Inventory inventory);
    
    /**
     * 根据 ID 查找库存
     */
    Optional<Inventory> findById(Long id);
    
    /**
     * 根据商品 ID 查找库存
     */
    Optional<Inventory> findByProductId(Long productId);
    
    /**
     * 根据仓库 ID 查询库存列表
     */
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    /**
     * 查询所有库存
     */
    List<Inventory> findAll();
    
    /**
     * 查询需要补货的库存
     */
    List<Inventory> findLowStockItems();
    
    /**
     * 更新库存
     */
    void update(Inventory inventory);
    
    /**
     * 删除库存记录
     */
    void delete(Long id);
}
