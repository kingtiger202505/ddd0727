package com.quotation.application.dto.order;

import lombok.Data;

/**
 * 订单多条件查询对象
 */
@Data
public class OrderQuery {
    private String orderNo;
    private String status;
    private Long buyerId;
    private Long sellerId;
    private Integer page = 1;
    private Integer size = 10;
}
