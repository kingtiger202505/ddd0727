package com.quotation.application.product.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU 数据传输对象
 */
@Data
public class SkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String skuCode;
    private String attrs; // 规格属性JSON
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stock;
    private Integer lockStock;
    private Integer availableStock;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
