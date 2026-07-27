package com.quotation.application.dto.order;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单 DTO
 */
@Data
public class OrderDTO {
    
    private Long id;
    private String orderNo;
    private Long buyerId;
    private String buyerName;
    private Long sellerId;
    private String sellerName;
    private Long quotationId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal actualAmount;
    private Integer status;
    private String statusText;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String shippingAddress;
    private String receiverName;
    private String receiverPhone;
    private String remark;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
