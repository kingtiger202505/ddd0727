package com.quotation.application.dto.order;

import lombok.Data;

/**
 * 取消订单命令
 */
@Data
public class CancelOrderCommand {
    
    private Long orderId;
    private String reason;
}
