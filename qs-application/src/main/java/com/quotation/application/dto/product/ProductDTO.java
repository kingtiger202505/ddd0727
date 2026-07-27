package com.quotation.application.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 DTO
 */
@Data
public class ProductDTO {
    
    private Long id;
    private Long sellerId;
    private String sellerName;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private BigDecimal minPrice;
    private Integer stockQuantity;
    private String unit;
    private String images;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
