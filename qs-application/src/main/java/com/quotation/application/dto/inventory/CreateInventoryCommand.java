package com.quotation.application.dto.inventory;

import lombok.Data;

/**
 * 创建库存命令
 */
@Data
public class CreateInventoryCommand {
    
    private Long productId;
    private Long warehouseId;
    private String warehouseName;
    private Integer quantity;
    private Integer minStock;
    private Integer maxStock;
}
