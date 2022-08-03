package cc.bucaedie.sample;

import cc.bucaedie.sample.context.OrderCreateContext;
import cc.bucaedie.sample.context.OrderOperationResult;
import cc.bucaedie.sample.service.OrderService;
import cc.bucaedie.ucede.commons.UUIDUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest(classes = BucaedieSampleApplication.class)
class BucaedieSampleApplicationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Test
    public void testCreate(){
        OrderCreateContext context = new OrderCreateContext();
        context.setConsumer("xxx");
        context.setOperator("xx");
        context.setOperationTime(new Date());
        context.setPrice(BigDecimal.valueOf(123123.23));
        context.setUuid(UUIDUtils.getUUID());
        context.setIdentity("XX22");
        OrderOperationResult result = orderService.createOrder(context);
    }


    @Test
    public void testPublish(){
        rabbitProducer.sendDemoQueue();
    }

    @Test
    void contextLoads() {
    }

}
