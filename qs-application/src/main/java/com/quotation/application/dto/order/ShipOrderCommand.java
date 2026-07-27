package com.quotation.application.dto.order;

import lombok.Data;

/**
 * 发货命令
 */
@Data
public class ShipOrderCommand {
    
    private Long orderId;
    private String logisticsCompany;
    private String logisticsNo;
}
