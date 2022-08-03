package cc.bucaedie.sample.service.impl;

import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.service.PayService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@UseCaseServiceExtension
public class PayServiceImpl implements PayService {
    @Override
    public OrderOperationResult pay(OrderOperationContext context) {
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

    @Override
    public void p() {

    }
}
