package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.inventory.model.Inventory;
import com.quotation.domain.inventory.repository.InventoryRepository;
import com.quotation.infrastructure.persistence.mapper.InventoryMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 库存仓储实现
 */
@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public void save(Inventory inventory) {
        inventoryMapper.insert(inventory);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return Optional.ofNullable(inventoryMapper.selectById(id));
    }

    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, productId);
        return Optional.ofNullable(inventoryMapper.selectOne(wrapper));
    }

    @Override
    public List<Inventory> findByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getWarehouseId, warehouseId);
        return inventoryMapper.selectList(wrapper);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryMapper.selectList(null);
    }

    @Override
    public List<Inventory> findLowStockItems() {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(Inventory::getAvailableQuantity, Inventory::getMinStock);
        return inventoryMapper.selectList(wrapper);
    }

    @Override
    public void update(Inventory inventory) {
        inventoryMapper.updateById(inventory);
    }

    @Override
    public void delete(Long id) {
        inventoryMapper.deleteById(id);
    }
}
