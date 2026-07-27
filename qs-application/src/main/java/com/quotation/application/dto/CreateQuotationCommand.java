package com.quotation.application.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建报价单命令对象
 */
@Data
public class CreateQuotationCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
