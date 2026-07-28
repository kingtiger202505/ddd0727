package com.quotation.domain.product.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU（库存量单位）领域模型
 */
@Data
public class Sku {
    private Long id;
    private Long productId;
    private String skuCode;
    private String attrs; // JSON格式：{"颜色":"红","尺寸":"L"}
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stock;
    private Integer lockStock;
    private Integer status; // 0-禁用 1-正常
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 锁定库存
     */
    public boolean lockStock(Integer quantity) {
        if (this.stock - this.lockStock >= quantity) {
            this.lockStock += quantity;
            return true;
        }
        return false;
    }

    /**
     * 释放锁定库存
     */
    public void releaseLockStock(Integer quantity) {
        if (this.lockStock >= quantity) {
            this.lockStock -= quantity;
        }
    }

    /**
     * 扣减库存
     */
    public boolean deductStock(Integer quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
            this.lockStock = Math.max(0, this.lockStock - quantity);
            return true;
        }
        return false;
    }

    /**
     * 获取可用库存
     */
    public Integer getAvailableStock() {
        return this.stock != null && this.lockStock != null ? this.stock - this.lockStock : 0;
    }
}
