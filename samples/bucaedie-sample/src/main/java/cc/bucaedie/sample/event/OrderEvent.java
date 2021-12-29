package cc.bucaedie.sample.event;

import cc.bucaedie.ucede.event.UseCaseEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent extends UseCaseEvent {

    /**
     * 父订单号
     */
    private String parentOrderId;
}
