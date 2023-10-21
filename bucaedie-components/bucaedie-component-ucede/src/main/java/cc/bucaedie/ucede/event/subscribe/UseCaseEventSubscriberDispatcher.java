package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.publiser.UseCaseEventPublisherManger;
import cc.bucaedie.ucede.usecase.UseCaseExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;
import java.util.List;

/**
 * 业务用例事件消费调用器
 */
@Slf4j
public class UseCaseEventSubscriberDispatcher implements ApplicationContextAware {

    @Autowired
    private UseCaseEventSubscriberRepository useCaseEventSubscribeRepository;

    @Autowired
    private UseCaseExecutor useCaseExecutor;

    @Autowired
    private UseCaseEventPublisherManger useCaseEventPublisherManger;
    /**
     * 调用业务事件
     * @param event
     */
    public  <T extends UseCaseEvent>  void dispatch(T event){
        // 获取事件监听
        List<UseCaseEventSubscriber> useCaseEventSubscriberList = useCaseEventSubscribeRepository.getUseCaseEventSubscriber(event);
        // 判断是否有事件监听者
        if (useCaseEventSubscriberList!=null && useCaseEventSubscriberList.size()>0){
            try {
                UseCaseEventSubscriberDispatchContextHolder.add(event);

                for (UseCaseEventSubscriber useCaseEventSubscriber : useCaseEventSubscriberList) {
                    // 此处包含数据的转换操作，数据转换为留给用户的接口，用户可能没有处理相关异常直接抛出给框架
                    try{
                        Object result = useCaseExecutor.execute(
                                useCaseEventSubscriber.getTriggerUseCaseDomain(),
                                useCaseEventSubscriber.getTriggerUseCaseService(),
                                useCaseEventSubscriber.getTriggerUseCase(),
                                event.getIdentity(),
                                getUseCaseEvent2ArgsConvertorByBeanName(useCaseEventSubscriber.getConvertorBeanName()).convert(event,useCaseEventSubscriber.getParameter()));
                        // 粗出需要判断retry
                    }catch (Exception e){
                        log.error("业务事件分发执行此业务事件失败,发生系统异常,业务单号:{},业务事件:{},异常信息：",event.getBizNo(),event.getEventUniqueKey(),e);
                        // 处理异常，记录事件日志，不要将异常抛出给监听,同时不会影响触发后续用例的执行
                        UseCaseEvent useCaseEvent = new UseCaseEvent();
                        useCaseEvent.setEvent("DISPATCHER_CONVERT_FAIL");
                        useCaseEvent.setUuid(UUIDUtils.getUUID());
                        useCaseEvent.setBizNo(event.getBizNo());
                        useCaseEvent.setIdentity(event.getIdentity());
                        useCaseEvent.setOperator("EVENT:DISPATCHER");
                        useCaseEvent.setOperationTime(new Date());
                        useCaseEvent.setMessage("业务事件:"+event.getEventUniqueKey()+"触发执行此业务用例失败,发生系统异常,位置主要为事件转换入参、获取业务身份及扩展等位置,此错误请勿使用业务用例重置功能,具体查看日志,异常信息："+e.getMessage());
                        useCaseEvent.setUseCaseCode(useCaseEventSubscriber.getTriggerUseCase());
                        useCaseEvent.setUseCaseServiceCode(useCaseEventSubscriber.getTriggerUseCaseService());
                        useCaseEvent.setDomain(useCaseEventSubscriber.getTriggerUseCaseDomain());
                        useCaseEvent.setEventTime(new Date());
                        useCaseEvent.setTriggerUuid(event.getTriggerUuid());
                        // 推送相关事件
                        useCaseEventPublisherManger.publishEvent(useCaseEvent);
                    }

                }
            }finally {
                UseCaseEventSubscriberDispatchContextHolder.remove();
            }

        }
    }

    private UseCaseEvent2ArgsConvertor getUseCaseEvent2ArgsConvertorByBeanName(String convertorBeanName){
        UseCaseEvent2ArgsConvertor convertor = applicationContext.getBean(convertorBeanName,UseCaseEvent2ArgsConvertor.class);
        if (convertor == null) {
            throw new IllegalArgumentException("为获取到BeanName为:" + convertorBeanName + " 对应的事件转换用例入参转换器");
        }
        return convertor;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
