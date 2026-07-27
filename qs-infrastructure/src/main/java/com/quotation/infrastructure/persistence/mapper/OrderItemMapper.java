package com.quotation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quotation.domain.order.model.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项 Mapper 接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
