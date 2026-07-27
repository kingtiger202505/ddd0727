package com.quotation.application.dto.order;

import lombok.Data;

/**
 * 支付订单命令
 */
@Data
public class PayOrderCommand {
    
    private Long orderId;
    private String paymentMethod; // ALIPAY, WECHAT, BANK_TRANSFER
}
