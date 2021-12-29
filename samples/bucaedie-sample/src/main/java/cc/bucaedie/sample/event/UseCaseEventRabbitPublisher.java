package cc.bucaedie.sample.event;

import cc.bucaedie.ucede.event.publiser.EventPublisher;
import cc.bucaedie.ucede.event.publiser.annotation.UseCaseEventPublisher;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  订单消息推送
 */
@UseCaseEventPublisher(domain = "ORDER")
public class UseCaseEventRabbitPublisher implements EventPublisher<OrderEvent> {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void publishEvent(OrderEvent event) {
        rabbitTemplate.convertAndSend("order_event", JSON.toJSONString(event));
    }
}
