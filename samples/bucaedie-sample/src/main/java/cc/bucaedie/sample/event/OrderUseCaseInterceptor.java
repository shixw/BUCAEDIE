package cc.bucaedie.sample.event;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.ucede.commons.IdentityConstants;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderUseCaseInterceptor implements UseCaseExecuteInterceptor<OrderEvent> {

    @Override
    public void before(UseCaseInfo useCaseInfo, Object[] args) {
        log.error("调用用例之前");
    }

    @Override
    public Object complete(UseCaseInfo useCaseInfo, Object[] args, Object result) {
        log.error("调用用例完成");
        return result;
    }

    @Override
    public OrderEvent convertorResult2Event(UseCaseInfo useCaseInfo, Object[] args, Object result) {
        OrderOperationResult resultTemp = (OrderOperationResult) result;
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setEvent(resultTemp.getCode()+"");
        orderEvent.setBizNo(resultTemp.getOrderNo());
        orderEvent.setIdentity(resultTemp.getIdentity());
        orderEvent.setMessage(resultTemp.getMessage());
        orderEvent.setData(JSON.toJSONString(resultTemp.getData()));
        orderEvent.setParam(JSON.toJSONString(args));
        orderEvent.setParentOrderId("XXXXX");
        return orderEvent;
    }

    @Override
    public Object exception(UseCaseInfo useCaseInfo, Object[] args, Exception exception) {
        OrderOperationResult result = new OrderOperationResult();
        result.setCode(0);
        result.setMessage("发生异常."+exception.getMessage());
        result.setSuccess(false);
        return result;
    }

    @Override
    public String getIdentity(UseCaseInfo useCaseInfo, Object[] args) {
        OrderOperationContext context = (OrderOperationContext) args[0];
        return context.getIdentity();
    }
}
