package com.quotation.domain.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体
 */
@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("seller_id")
    private Long sellerId;
    
    @TableField("name")
    private String name;
    
    @TableField("description")
    private String description;
    
    @TableField("category_id")
    private Long categoryId;
    
    @TableField("category_name")
    private String categoryName;
    
    @TableField("base_price")
    private BigDecimal basePrice;
    
    @TableField("min_price")
    private BigDecimal minPrice;
    
    @TableField("stock_quantity")
    private Integer stockQuantity;
    
    @TableField("unit")
    private String unit;
    
    @TableField("images")
    private String images; // JSON 字符串
    
    @TableField("status")
    private Integer status; // 0-下架，1-上架
    
    @Version
    @TableField("version")
    private Integer version;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 创建商品
     */
    public static Product create(Long sellerId, String name, String description, 
                                  Long categoryId, String categoryName,
                                  BigDecimal basePrice, BigDecimal minPrice,
                                  Integer stockQuantity, String unit) {
        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(name);
        product.setDescription(description);
        product.setCategoryId(categoryId);
        product.setCategoryName(categoryName);
        product.setBasePrice(basePrice);
        product.setMinPrice(minPrice != null ? minPrice : basePrice.multiply(new BigDecimal("0.8")));
        product.setStockQuantity(stockQuantity);
        product.setUnit(unit);
        product.setStatus(1);
        product.setVersion(1);
        return product;
    }
    
    /**
     * 上架商品
     */
    public void enable() {
        this.status = 1;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 下架商品
     */
    public void disable() {
        this.status = 0;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 更新库存
     */
    public void updateStock(int quantity) {
        if (this.stockQuantity + quantity < 0) {
            throw new IllegalStateException("库存不足");
        }
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
