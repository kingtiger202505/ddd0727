package com.quotation.infrastructure.payment;

import com.quotation.domain.order.model.Order;
import com.quotation.domain.order.model.PaymentMethod;
import com.quotation.domain.order.model.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String createPayment(Order order, PaymentMethod method) {
        if (method == PaymentMethod.WECHAT) {
            return createWechatPayment(order);
        } else if (method == PaymentMethod.ALIPAY) {
            return createAlipayPayment(order);
        }
        throw new IllegalArgumentException("不支持的支付方式");
    }

    private String createWechatPayment(Order order) {
        String nonceStr = java.util.UUID.randomUUID().toString().replace("-", "");
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println("=== 微信支付下单 === 订单号：" + order.getOrderNo());
        return String.format("{\"appId\":\"wx8888888888888888\",\"timeStamp\":\"%s\",\"nonceStr\":\"%s\"}", timeStamp, nonceStr);
    }

    private String createAlipayPayment(Order order) {
        System.out.println("=== 支付宝支付下单 === 订单号：" + order.getOrderNo());
        return "https://openapi.alipay.com/gateway.do?out_trade_no=" + order.getOrderNo();
    }

    public boolean handleCallback(String orderNo, PaymentStatus status) {
        System.out.println("=== 支付回调 === 订单号：" + orderNo + " 状态：" + status);
        return status == PaymentStatus.SUCCESS;
    }

    public PaymentStatus queryStatus(String orderNo) {
        return PaymentStatus.SUCCESS;
    }
}
