package com.quotation.application.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CreateQuotationCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long sellerId;
    private Long buyerId;
    private Long price;
    private String productName;
}
