package com.quotation.application.service;

import cn.dev33.satoken.stp.StpUtil;
import com.quotation.application.dto.order.*;
import com.quotation.common.exception.BusinessException;
import com.quotation.domain.inventory.model.Inventory;
import com.quotation.domain.inventory.repository.InventoryRepository;
import com.quotation.domain.order.model.Order;
import com.quotation.domain.order.model.OrderItem;
import com.quotation.domain.order.repository.OrderRepository;
import com.quotation.domain.product.model.Product;
import com.quotation.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 订单应用服务
 */
@Service
public class OrderApplicationService {

    @Resource
    private OrderRepository orderRepository;
    
    @Resource
    private InventoryRepository inventoryRepository;
    
    @Resource
    private ProductRepository productRepository;

    /**
     * 创建订单
     */
    @Transactional
    public Long createOrder(CreateOrderCommand command) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreateOrderCommand.OrderItemCommand itemCmd : command.getItems()) {
            totalAmount = totalAmount.add(itemCmd.getUnitPrice().multiply(new BigDecimal(itemCmd.getQuantity())));
        }
        
        // 创建订单
        Order order = Order.create(
            orderNo,
            currentUserId,
            null, // sellerId 从报价单获取
            command.getQuotationId(),
            totalAmount,
            BigDecimal.ZERO,
            command.getShippingAddress(),
            command.getReceiverName(),
            command.getReceiverPhone()
        );
        order.setRemark(command.getRemark());
        
        orderRepository.save(order);
        
        // 创建订单项并锁定库存
        for (CreateOrderCommand.OrderItemCommand itemCmd : command.getItems()) {
            Product product = productRepository.findById(itemCmd.getProductId())
                .orElseThrow(() -> new BusinessException("商品不存在：" + itemCmd.getProductId()));
            
            OrderItem item = OrderItem.create(
                order.getId(),
                product.getId(),
                product.getName(),
                product.getImages(),
                itemCmd.getQuantity(),
                itemCmd.getUnitPrice()
            );
            // 保存订单项（需要 OrderItemMapper）
            
            // 锁定库存
            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                .orElseThrow(() -> new BusinessException("商品库存不足：" + product.getName()));
            inventory.lockStock(itemCmd.getQuantity());
            inventoryRepository.update(inventory);
        }
        
        return order.getId();
    }

    /**
     * 获取订单详情
     */
    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    /**
     * 根据订单号获取订单
     */
    public OrderDTO getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
            .map(this::convertToDTO)
            .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    /**
     * 获取我的订单（买家）
     */
    public List<OrderDTO> getMyOrders() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return orderRepository.findByBuyerId(currentUserId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 获取卖家订单
     */
    public List<OrderDTO> getSellerOrders() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return orderRepository.findBySellerId(currentUserId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 支付订单
     */
    @Transactional
    public void payOrder(PayOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
            .orElseThrow(() -> new BusinessException("订单不存在"));
        
        order.pay(command.getPaymentMethod());
        orderRepository.update(order);
    }

    /**
     * 发货
     */
    @Transactional
    public void shipOrder(ShipOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
            .orElseThrow(() -> new BusinessException("订单不存在"));
        
        order.ship();
        orderRepository.update(order);
    }

    /**
     * 确认收货
     */
    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException("订单不存在"));
        
        // 验证是否是买家
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!order.getBuyerId().equals(currentUserId)) {
            throw new BusinessException("无权确认收货");
        }
        
        order.complete();
        orderRepository.update(order);
    }

    /**
     * 取消订单
     */
    @Transactional
    public void cancelOrder(CancelOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
            .orElseThrow(() -> new BusinessException("订单不存在"));
        
        // 验证是否是买家
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!order.getBuyerId().equals(currentUserId)) {
            throw new BusinessException("无权取消订单");
        }
        
        order.cancel();
        orderRepository.update(order);
        
        // 释放锁定的库存
        // 这里简化处理，实际需要遍历订单项释放
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateStr = now.format(formatter);
        Random random = new Random();
        int randomNum = 1000 + random.nextInt(9000);
        return "ORD" + dateStr + randomNum;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setBuyerId(order.getBuyerId());
        dto.setSellerId(order.getSellerId());
        dto.setQuotationId(order.getQuotationId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setActualAmount(order.getActualAmount());
        dto.setStatus(order.getStatus());
        dto.setStatusText(getStatusText(order.getStatus()));
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentTime(order.getPaymentTime());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setReceiverName(order.getReceiverName());
        dto.setReceiverPhone(order.getReceiverPhone());
        dto.setRemark(order.getRemark());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "已支付";
            case 2: return "待发货";
            case 3: return "已发货";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知";
        }
    }
}
