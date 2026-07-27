package com.quotation.application.dto.inventory;

import lombok.Data;

/**
 * 更新库存命令
 */
@Data
public class UpdateInventoryCommand {
    
    private Long id;
    private Integer quantity;
    private Integer minStock;
    private Integer maxStock;
    private String operationType; // ADD, DEDUCT, LOCK, RELEASE
}
