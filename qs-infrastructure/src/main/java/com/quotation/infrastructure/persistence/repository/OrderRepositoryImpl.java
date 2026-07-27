package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.order.model.Order;
import com.quotation.domain.order.repository.OrderRepository;
import com.quotation.infrastructure.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 订单仓储实现
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void save(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderMapper.selectById(id));
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        return Optional.ofNullable(orderMapper.selectOne(wrapper));
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyerId, buyerId)
               .orderByDesc(Order::getCreatedAt);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getSellerId, sellerId)
               .orderByDesc(Order::getCreatedAt);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public Optional<Order> findByQuotationId(Long quotationId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getQuotationId, quotationId);
        return Optional.ofNullable(orderMapper.selectOne(wrapper));
    }

    @Override
    public List<Order> findAll() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getCreatedAt);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public void update(Order order) {
        orderMapper.updateById(order);
    }

    @Override
    public void delete(Long id) {
        orderMapper.deleteById(id);
    }
}
