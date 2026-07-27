package com.quotation.application.dto.inventory;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存 DTO
 */
@Data
public class InventoryDTO {
    
    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Integer quantity;
    private Integer lockedQuantity;
    private Integer availableQuantity;
    private Integer minStock;
    private Integer maxStock;
    private Boolean needsReplenishment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
