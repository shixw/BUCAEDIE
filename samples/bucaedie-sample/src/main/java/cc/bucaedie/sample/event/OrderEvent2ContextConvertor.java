package cc.bucaedie.sample.event;

import cc.bucaedie.sample.context.OrderOperationContext;
import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.subscribe.UseCaseEvent2ArgsConvertor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("orderEvent2ContextConvertor")
public class OrderEvent2ContextConvertor implements UseCaseEvent2ArgsConvertor {


    @Override
    public Object[] convert(UseCaseEvent event) {
        OrderOperationContext context = new OrderOperationContext();
        context.setOrderNo(event.getBizNo());
        context.setOperator("SYS");
        context.setOperationTime(new Date());
        context.setUuid(UUIDUtils.getUUID());
        return new Object[]{context};
    }
}
