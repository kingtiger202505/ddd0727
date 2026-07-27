package com.quotation.application.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报价单数据传输对象
 */
@Data
public class QuotationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private Integer version;
    private Date createTime;
    private Date updateTime;
}
