package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.event.UseCaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务事件订阅者关系仓库
 */
@Slf4j
public class UseCaseEventSubscriberRepository implements InitializingBean, ApplicationContextAware {

    /**
     * 业务事件订阅关系
     */
    private Map<String, UseCaseEventIdentitySubscriber> useCaseEventIdentitySubscriberRepo = new ConcurrentHashMap<>();


    public List<UseCaseEventSubscriber> getUseCaseEventSubscriber(UseCaseEvent event) {
        return getUseCaseEventSubscriber(event.getIdentity(), event.getDomain(), event.getEvent());
    }

        /**
         * 根据业务身份，领域，事件，获取事件对应的订阅者
         * @param identity
         * @param domain
         * @param event
         * @return
         */
    public List<UseCaseEventSubscriber> getUseCaseEventSubscriber(String identity, String domain, String event){
        if (identity==null || !useCaseEventIdentitySubscriberRepo.containsKey(identity)){
            log.error("业务身份ID为空,或为获取到业务身份对应的业务用例事件订阅关系");
            return null;
        }
        // 业务身份监听信息
        UseCaseEventIdentitySubscriber identitySubscriber = useCaseEventIdentitySubscriberRepo.get(identity);
        // 获取业务身份对应的事件监听关系
        Map<String, List<UseCaseEventSubscriber>> useCaseEventSubscriberMap = identitySubscriber.getUseCaseEventSubscriberMap();
        if (useCaseEventSubscriberMap==null || useCaseEventSubscriberMap.size()<=0){
            return null;
        }
        String eventUniqueKey = UseCaseEvent.getEventUniqueKey(domain, event);
        if (!useCaseEventSubscriberMap.containsKey(eventUniqueKey)){
            return null;
        }
        // 返回订阅关系集合
        return useCaseEventSubscriberMap.get(eventUniqueKey);
    }

    /**
     * 添加业务身份维度的定于关系
     */
    public void addUseCaseEventIdentitySubscriber(UseCaseEventIdentitySubscriber identitySubscriber){
        useCaseEventIdentitySubscriberRepo.put(identitySubscriber.getIdentity(),identitySubscriber);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, UseCaseEventIdentitySubscriber> serviceBillFlowMap = applicationContext.getBeansOfType(UseCaseEventIdentitySubscriber.class);

        if (serviceBillFlowMap!=null && serviceBillFlowMap.size()>0){
            for (UseCaseEventIdentitySubscriber subscriber : serviceBillFlowMap.values()) {
                useCaseEventIdentitySubscriberRepo.put(subscriber.getIdentity(), subscriber);
            }
        }
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
