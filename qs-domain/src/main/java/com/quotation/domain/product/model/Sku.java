package com.quotation.domain.product.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU（库存量单位）领域模型
 */
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

    public Sku() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getLockStock() { return lockStock; }
    public void setLockStock(Integer lockStock) { this.lockStock = lockStock; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

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
        return this.stock - this.lockStock;
    }
}
