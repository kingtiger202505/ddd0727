package com.quotation.application.dto.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单命令
 */
@Data
public class CreateOrderCommand {
    
    private Long quotationId;
    private String shippingAddress;
    private String receiverName;
    private String receiverPhone;
    private String remark;
    private List<OrderItemCommand> items;
    
    @Data
    public static class OrderItemCommand {
        private Long productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
