package cc.bucaedie.sample.service;

import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.event.OrderUseCaseInterceptor;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;

@UseCaseService(domain = "ORDER",serviceCode = "payService",interceptor = OrderUseCaseInterceptor.class)
public interface PayService {


    OrderOperationResult pay(OrderOperationContext context);

    void p();
}
