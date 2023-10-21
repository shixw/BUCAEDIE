package cc.bucaedie.sample.service;

import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.event.OrderUseCaseInterceptor;
import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import org.springframework.beans.factory.annotation.Autowired;

@UseCaseService(domain = "ORDER",serviceCode = "payService",interceptor = OrderUseCaseInterceptor.class)
public interface PayService {

    @UseCase(code = "pay",desc = "支付",events = {"3"})
    OrderOperationResult pay(OrderOperationContext context);

    void p();
}
