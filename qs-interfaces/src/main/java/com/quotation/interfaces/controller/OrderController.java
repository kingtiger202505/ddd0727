package com.quotation.interfaces.controller;

import com.quotation.application.dto.order.*;
import com.quotation.application.service.OrderApplicationService;
import com.quotation.interfaces.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单管理控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Resource
    private OrderApplicationService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public R<Long> create(@RequestBody CreateOrderCommand command) {
        Long orderId = orderService.createOrder(command);
        return R.ok(orderId);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{id}")
    public R<OrderDTO> detail(@PathVariable Long id) {
        return R.ok(orderService.getOrderById(id));
    }

    /**
     * 根据订单号获取订单
     */
    @GetMapping("/order-no/{orderNo}")
    public R<OrderDTO> getByOrderNo(@PathVariable String orderNo) {
        return R.ok(orderService.getOrderByOrderNo(orderNo));
    }

    /**
     * 获取我的订单（买家）
     */
    @GetMapping("/my-orders")
    public R<List<OrderDTO>> myOrders() {
        return R.ok(orderService.getMyOrders());
    }

    /**
     * 获取卖家订单
     */
    @GetMapping("/seller-orders")
    public R<List<OrderDTO>> sellerOrders() {
        return R.ok(orderService.getSellerOrders());
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    public R<Boolean> pay(@RequestBody PayOrderCommand command) {
        orderService.payOrder(command);
        return R.ok(true);
    }

    /**
     * 发货
     */
    @PostMapping("/ship")
    public R<Boolean> ship(@RequestBody ShipOrderCommand command) {
        orderService.shipOrder(command);
        return R.ok(true);
    }

    /**
     * 确认收货
     */
    @PostMapping("/complete/{orderId}")
    public R<Boolean> complete(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return R.ok(true);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public R<Boolean> cancel(@RequestBody CancelOrderCommand command) {
        orderService.cancelOrder(command);
        return R.ok(true);
    }
}
