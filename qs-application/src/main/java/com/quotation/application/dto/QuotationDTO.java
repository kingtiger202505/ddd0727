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
    private String quotationNo;
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String customerName;
    private String productName;
    private BigDecimal price;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private Integer version;
    private Date createTime;
    private Date updateTime;
}
