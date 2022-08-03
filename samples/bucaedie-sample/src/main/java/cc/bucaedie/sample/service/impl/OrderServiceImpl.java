package cc.bucaedie.sample.service.impl;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.event.OrderUseCaseInterceptor;
import cc.bucaedie.sample.service.OrderService;
import cc.bucaedie.sample.service.PayService;
import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
@UseCaseServiceExtension
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PayService payService;

    @Override
    public OrderOperationResult createOrder(OrderCreateContext createContext) {
        System.out.println("创建订单");
        for (int i = 0; i < 2; i++) {
            OrderOperationContext context = new OrderOperationContext();
            context.setOperator("xx");
            context.setOperationTime(new Date());
            context.setUuid(UUIDUtils.getUUID());
            context.setIdentity("XX22");
            payService.pay(context);
        }
        OrderOperationResult result = new OrderOperationResult();
        result.setCode(1);
        result.setMessage("成功");
        result.setOperator(createContext.getOperator());
        result.setOperationTime(createContext.getOperationTime());
        result.setUuid(createContext.getUuid());
        result.setSuccess(true);
        result.setOrderNo("1xxxas13");
        result.setIdentity("ORDER");
        return result;
    }

    @Override
    public OrderOperationResult sendSms(OrderOperationContext context) {
        log.error("发送短信成功-=======");
        OrderOperationResult result = new OrderOperationResult();
        result.setCode(2);
        result.setMessage("成功");
        result.setOperator(context.getOperator());
        result.setOperationTime(context.getOperationTime());
        result.setUuid(context.getUuid());
        result.setSuccess(true);
        result.setOrderNo("1xxxas13");
        result.setIdentity("ORDER");
        return result;
    }


}
