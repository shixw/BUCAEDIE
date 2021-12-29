package cc.bucaedie.ucede.event.store;

import cc.bucaedie.ucede.event.UseCaseEvent;

/**
 * 业务用例事件存储Service
 */
public interface UseCaseEventStoreService<T extends UseCaseEvent>  {

    void storeUseCaseEvent(T event);
}
