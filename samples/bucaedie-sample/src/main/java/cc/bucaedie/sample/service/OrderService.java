package cc.bucaedie.sample.service;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;

/**
 * 订单服务
 */
public interface OrderService {

    /**
     * 创建订单
     */
    OrderOperationResult createOrder(OrderCreateContext createContext);

    OrderOperationResult sendSms(OrderOperationContext context);
}
