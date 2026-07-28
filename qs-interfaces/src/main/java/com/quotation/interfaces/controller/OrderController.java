package com.quotation.interfaces.controller;

import com.quotation.application.dto.order.*;
import com.quotation.application.service.OrderApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单管理控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping({"/api/orders", "/api/order"})
public class OrderController {

    @Resource
    private OrderApplicationService orderService;

    /**
     * 多条件分页查询订单列表（PC管理端）
     */
    @GetMapping("/list")
    public Result<List<OrderDTO>> list(OrderQuery query) {
        List<OrderDTO> list = orderService.queryOrders(query);
        return Result.success(list);
    }

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody CreateOrderCommand command) {
        Long orderId = orderService.createOrder(command);
        return Result.success(orderId);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{id}")
    public Result<OrderDTO> detail(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    /**
     * 根据订单号获取订单
     */
    @GetMapping("/order-no/{orderNo}")
    public Result<OrderDTO> getByOrderNo(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByOrderNo(orderNo));
    }

    /**
     * 获取我的订单（买家）
     */
    @GetMapping("/my-orders")
    public Result<List<OrderDTO>> myOrders() {
        return Result.success(orderService.getMyOrders());
    }

    /**
     * 获取卖家订单
     */
    @GetMapping("/seller-orders")
    public Result<List<OrderDTO>> sellerOrders() {
        return Result.success(orderService.getSellerOrders());
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    public Result<Boolean> pay(@RequestBody PayOrderCommand command) {
        orderService.payOrder(command);
        return Result.success(true);
    }

    /**
     * 发货
     */
    @PostMapping("/ship")
    public Result<Boolean> ship(@RequestBody ShipOrderCommand command) {
        orderService.shipOrder(command);
        return Result.success(true);
    }

    /**
     * 确认收货
     */
    @PostMapping("/complete/{orderId}")
    public Result<Boolean> complete(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return Result.success(true);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public Result<Boolean> cancel(@RequestBody CancelOrderCommand command) {
        orderService.cancelOrder(command);
        return Result.success(true);
    }
}
