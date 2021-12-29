package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.store.UseCaseEventStoreService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用例事件监听
 */
public class UseCaseEventListener {

    @Autowired
    private UseCaseEventStoreService useCaseEventStoreService;

    @Autowired
    private UseCaseEventSubscriberDispatcher dispatcher;

    public  <T extends UseCaseEvent> void onEvent(T event){
        if (event!=null){
            // 事件存储
            useCaseEventStoreService.storeUseCaseEvent(event);
            // 分发事件
            dispatcher.dispatch(event);
        }
    }
}
