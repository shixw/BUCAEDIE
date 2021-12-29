package cc.bucaedie.sample.event;

import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.store.UseCaseEventStoreService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UseCaseEventStoreServiceImpl implements UseCaseEventStoreService<UseCaseEvent> {
    @Override
    public void storeUseCaseEvent(UseCaseEvent event) {
        log.error("业务事件存储：{}", JSON.toJSONString(event));
    }
}
