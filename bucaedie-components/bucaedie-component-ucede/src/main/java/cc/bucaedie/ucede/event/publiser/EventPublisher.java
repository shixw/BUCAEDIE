package cc.bucaedie.ucede.event.publiser;

import cc.bucaedie.ucede.event.UseCaseEvent;

/**
 * 业务用例事件publisher
 */
public interface EventPublisher<T extends UseCaseEvent> {

     void publishEvent(T event);
}
