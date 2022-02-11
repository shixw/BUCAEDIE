package cc.bucaedie.sample.event;

import cc.bucaedie.ucede.event.publiser.EventPublisher;
import cc.bucaedie.ucede.event.publiser.annotation.UseCaseEventPublisher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  订单消息推送
 */
@UseCaseEventPublisher(domain = "ORDER")
@Slf4j
public class UseCaseEventRabbitPublisher implements EventPublisher<OrderEvent> {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void publishEvent(OrderEvent event) {
        String message = JSON.toJSONString(event);
        log.error("推送订单业务事件：{}",message);
        rabbitTemplate.convertAndSend("order_event", message);
    }
}
