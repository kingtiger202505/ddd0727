package com.quotation.domain.order.repository;

import com.quotation.domain.order.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * 订单仓储接口
 */
public interface OrderRepository {
    
    /**
     * 保存订单
     */
    void save(Order order);
    
    /**
     * 根据 ID 查找订单
     */
    Optional<Order> findById(Long id);
    
    /**
     * 根据订单号查找订单
     */
    Optional<Order> findByOrderNo(String orderNo);
    
    /**
     * 根据买家 ID 查询订单列表
     */
    List<Order> findByBuyerId(Long buyerId);
    
    /**
     * 根据卖家 ID 查询订单列表
     */
    List<Order> findBySellerId(Long sellerId);
    
    /**
     * 根据报价单 ID 查询订单
     */
    Optional<Order> findByQuotationId(Long quotationId);
    
    /**
     * 多条件查询订单列表
     */
    List<Order> findByConditions(String orderNo, String status, Long buyerId, Long sellerId);

    /**
     * 查询所有订单
     */
    List<Order> findAll();
    
    /**
     * 更新订单
     */
    void update(Order order);
    
    /**
     * 删除订单
     */
    void delete(Long id);
}
