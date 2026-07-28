package com.quotation.application.convert;

import com.quotation.application.dto.order.CreateOrderCommand;
import com.quotation.application.dto.order.OrderDTO;
import com.quotation.domain.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderConvert {
    OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);

    OrderDTO toDTO(Order order);

    Order toEntity(CreateOrderCommand command);

    List<OrderDTO> toDTOList(List<Order> orderList);
}
