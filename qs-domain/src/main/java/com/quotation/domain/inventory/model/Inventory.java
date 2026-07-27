package com.quotation.domain.inventory.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存实体
 */
@Data
@TableName("inventory")
public class Inventory {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("product_id")
    private Long productId;
    
    @TableField("warehouse_id")
    private Long warehouseId;
    
    @TableField("warehouse_name")
    private String warehouseName;
    
    @TableField("quantity")
    private Integer quantity;
    
    @TableField("locked_quantity")
    private Integer lockedQuantity;
    
    @TableField("available_quantity")
    private Integer availableQuantity;
    
    @TableField("min_stock")
    private Integer minStock;
    
    @TableField("max_stock")
    private Integer maxStock;
    
    @Version
    @TableField("version")
    private Integer version;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 创建库存记录
     */
    public static Inventory create(Long productId, Long warehouseId, String warehouseName, 
                                   Integer quantity, Integer minStock, Integer maxStock) {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setWarehouseId(warehouseId);
        inventory.setWarehouseName(warehouseName);
        inventory.setQuantity(quantity);
        inventory.setLockedQuantity(0);
        inventory.setAvailableQuantity(quantity);
        inventory.setMinStock(minStock != null ? minStock : 10);
        inventory.setMaxStock(maxStock != null ? maxStock : 1000);
        inventory.setVersion(1);
        return inventory;
    }
    
    /**
     * 锁定库存（用于订单创建）
     */
    public void lockStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("锁定数量必须大于 0");
        }
        if (this.availableQuantity < quantity) {
            throw new IllegalStateException("可用库存不足，无法锁定");
        }
        this.lockedQuantity += quantity;
        this.availableQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 释放锁定库存（用于订单取消）
     */
    public void releaseLockedStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("释放数量必须大于 0");
        }
        if (this.lockedQuantity < quantity) {
            throw new IllegalStateException("锁定库存不足，无法释放");
        }
        this.lockedQuantity -= quantity;
        this.availableQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 扣减库存（用于订单确认）
     */
    public void deductStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("扣减数量必须大于 0");
        }
        if (this.lockedQuantity < quantity) {
            throw new IllegalStateException("锁定库存不足，无法扣减");
        }
        this.quantity -= quantity;
        this.lockedQuantity -= quantity;
        this.availableQuantity = this.quantity - this.lockedQuantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 增加库存（用于入库或退货）
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("增加数量必须大于 0");
        }
        this.quantity += quantity;
        this.availableQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 检查库存是否充足
     */
    public boolean isStockSufficient(int quantity) {
        return this.availableQuantity >= quantity;
    }
    
    /**
     * 检查是否需要补货
     */
    public boolean needsReplenishment() {
        return this.availableQuantity <= this.minStock;
    }
}
