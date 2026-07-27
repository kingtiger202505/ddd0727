package com.quotation.domain.order.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体
 */
@Data
@TableName("orders")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("buyer_id")
    private Long buyerId;
    
    @TableField("seller_id")
    private Long sellerId;
    
    @TableField("quotation_id")
    private Long quotationId;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField("discount_amount")
    private BigDecimal discountAmount;
    
    @TableField("actual_amount")
    private BigDecimal actualAmount;
    
    @TableField("status")
    private Integer status; // 0-待支付，1-已支付，2-待发货，3-已发货，4-已完成，5-已取消
    
    @TableField("payment_method")
    private String paymentMethod;
    
    @TableField("payment_time")
    private LocalDateTime paymentTime;
    
    @TableField("shipping_address")
    private String shippingAddress;
    
    @TableField("receiver_name")
    private String receiverName;
    
    @TableField("receiver_phone")
    private String receiverPhone;
    
    @TableField("remark")
    private String remark;
    
    @Version
    @TableField("version")
    private Integer version;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableField(exist = false)
    private List<OrderItem> items = new ArrayList<>();
    
    /**
     * 创建订单
     */
    public static Order create(String orderNo, Long buyerId, Long sellerId, Long quotationId,
                               BigDecimal totalAmount, BigDecimal discountAmount, 
                               String shippingAddress, String receiverName, String receiverPhone) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setBuyerId(buyerId);
        order.setSellerId(sellerId);
        order.setQuotationId(quotationId);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        order.setActualAmount(totalAmount.subtract(order.getDiscountAmount()));
        order.setStatus(0); // 待支付
        order.setShippingAddress(shippingAddress);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setVersion(1);
        return order;
    }
    
    /**
     * 添加订单项
     */
    public void addItem(OrderItem item) {
        this.items.add(item);
    }
    
    /**
     * 支付订单
     */
    public void pay(String paymentMethod) {
        if (this.status != 0) {
            throw new IllegalStateException("订单状态不允许支付");
        }
        this.status = 1;
        this.paymentMethod = paymentMethod;
        this.paymentTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 确认发货
     */
    public void ship() {
        if (this.status != 1 && this.status != 2) {
            throw new IllegalStateException("订单状态不允许发货");
        }
        this.status = 3;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 确认收货（完成订单）
     */
    public void complete() {
        if (this.status != 3) {
            throw new IllegalStateException("订单状态不允许完成");
        }
        this.status = 4;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 取消订单
     */
    public void cancel() {
        if (this.status == 4 || this.status == 5) {
            throw new IllegalStateException("订单已完成或已取消，无法取消");
        }
        this.status = 5;
        this.updatedAt = LocalDateTime.now();
    }
}
