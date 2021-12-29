package cc.bucaedie.sample;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//类型为fanout
@Configuration
public class PublishInitialization {

    /**
     * 定义demoQueue队列
     * @return
     */
    @Bean
    public Queue demoString() {
        return new Queue("demoQueue");
    }

    @Bean
    public Queue order_event() {
        return new Queue("order_event");
    }
}
