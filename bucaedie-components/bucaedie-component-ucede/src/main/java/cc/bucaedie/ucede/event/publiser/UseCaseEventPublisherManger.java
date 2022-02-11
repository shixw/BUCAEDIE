package cc.bucaedie.ucede.event.publiser;

import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.publiser.annotation.UseCaseEventPublisher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务用例事件publisher管理器
 */
@Slf4j
public class UseCaseEventPublisherManger<T extends UseCaseEvent>  implements InitializingBean, ApplicationContextAware {

     public void publishEvent(T event){
          String domain = event.getDomain();
          if (publisherRepo.containsKey(domain)){
               try {
                    publisherRepo.get(domain).publishEvent(event);
               } catch (Exception e) {
                    log.error("推送业务用例失败失败,事件:{},失败原因:", JSON.toJSONString(event),e);
               }
          }
     }

     /**
      * 用来存储不同业务domain对应的事件推送
      */
     private Map<String, EventPublisher> publisherRepo = new ConcurrentHashMap<>();


     @Override
     public void afterPropertiesSet() throws Exception {
          Map<String, EventPublisher> useCaseEventPublisherMap = applicationContext.getBeansOfType(EventPublisher.class);

          if (useCaseEventPublisherMap!=null && useCaseEventPublisherMap.size()>0){
               for (EventPublisher eventPublisher : useCaseEventPublisherMap.values()) {
                    Class<?> userCaseServiceClass = eventPublisher.getClass();
                    UseCaseEventPublisher useCaseEventPublisherAnnotation = AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseEventPublisher.class);
                    if (useCaseEventPublisherAnnotation==null){
                         throw new IllegalArgumentException("为获取到publisher对应的注解信息，业务事件推送器需要使用注解 UseCaseEventPublisher 标注");
                    }
                    String domain = useCaseEventPublisherAnnotation.domain();
                    publisherRepo.put(domain, eventPublisher);
               }
          }
     }

     private ApplicationContext applicationContext;

     @Override
     public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          this.applicationContext = applicationContext;
     }
}
