package cc.bucaedie.sample.service;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.event.OrderUseCaseInterceptor;
import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;

/**
 * 订单服务
 */
@UseCaseService(domain = "ORDER",serviceCode = "orderService",interceptor = OrderUseCaseInterceptor.class)
public interface OrderService {

    /**
     * 创建订单
     */
    @UseCase(code = "createOrder",desc = "创建订单",events = {"1"})
    OrderOperationResult createOrder(OrderCreateContext createContext);

    @UseCase(code = "sendSms",desc = "发送短信",events = {"2"})
    OrderOperationResult sendSms(OrderOperationContext context);
}
