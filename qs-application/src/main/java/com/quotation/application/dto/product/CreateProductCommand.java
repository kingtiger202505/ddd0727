package com.quotation.application.dto.product;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 创建商品命令
 */
@Data
public class CreateProductCommand {
    
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private BigDecimal minPrice;
    private Integer stockQuantity;
    private String unit;
    private String images;
}
