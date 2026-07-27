package com.quotation.domain.order.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单项实体
 */
@Data
@TableName("order_items")
public class OrderItem {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_id")
    private Long orderId;
    
    @TableField("product_id")
    private Long productId;
    
    @TableField("product_name")
    private String productName;
    
    @TableField("product_image")
    private String productImage;
    
    @TableField("quantity")
    private Integer quantity;
    
    @TableField("unit_price")
    private BigDecimal unitPrice;
    
    @TableField("total_price")
    private BigDecimal totalPrice;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 创建订单项
     */
    public static OrderItem create(Long orderId, Long productId, String productName, 
                                   String productImage, Integer quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setProductId(productId);
        item.setProductName(productName);
        item.setProductImage(productImage);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setTotalPrice(unitPrice.multiply(new BigDecimal(quantity)));
        return item;
    }
}
