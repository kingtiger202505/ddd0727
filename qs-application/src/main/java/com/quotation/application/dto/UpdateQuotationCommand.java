package com.quotation.application.dto;

import lombok.Data;

/**
 * 更新报价单命令
 */
@Data
public class UpdateQuotationCommand {
    
    private Long id;
    
    /**
     * 客户名称
     */
    private String customerName;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 单价
     */
    private java.math.BigDecimal unitPrice;
}
