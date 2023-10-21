package cc.bucaedie.sample.service.impl;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.service.OrderService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@UseCaseServiceExtension(identities = {"XX"})
public class OrderServiceXXImpl implements OrderService {

    @Override
    public OrderOperationResult createOrder(OrderCreateContext createContext) {
        System.out.println("XX业务身份创建订单");
        OrderOperationResult result = new OrderOperationResult();
        result.setCode(1);
        result.setMessage("成功");
        result.setOperator(createContext.getOperator());
        result.setOperationTime(createContext.getOperationTime());
        result.setUuid(createContext.getUuid());
        result.setSuccess(true);
        result.setOrderNo("1xxxas13");
        result.setIdentity("ORDER");
        throw new RuntimeException("xxxx");

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
