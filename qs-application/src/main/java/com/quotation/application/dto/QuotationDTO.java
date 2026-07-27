package com.quotation.application.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class QuotationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Long price;
    private String status;
    private Integer version;
    private Date createTime;
    private Date updateTime;
}
