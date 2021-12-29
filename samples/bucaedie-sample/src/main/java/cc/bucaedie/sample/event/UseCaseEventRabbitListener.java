package cc.bucaedie.sample.event;

import cc.bucaedie.ucede.event.subscribe.UseCaseEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "order_event")
@Slf4j
public class UseCaseEventRabbitListener {

    @Autowired
    private UseCaseEventListener useCaseEventListener;

    /**
     * 消息消费
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void recieved(String event) {

        OrderEvent orderEvent = JSON.parseObject(event, OrderEvent.class);
        useCaseEventListener.onEvent(orderEvent);
    }
}
