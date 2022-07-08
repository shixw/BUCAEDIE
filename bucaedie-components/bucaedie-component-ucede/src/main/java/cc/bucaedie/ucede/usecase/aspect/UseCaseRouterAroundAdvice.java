package cc.bucaedie.ucede.usecase.aspect;

import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.publiser.UseCaseEventPublisherManger;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventSubscriberDispatchContextHolder;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseExecutor;
import cc.bucaedie.ucede.usecase.UseCaseInfo;
import cc.bucaedie.ucede.usecase.UseCaseRepository;
import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 业务用例路由拦截器
 */
@Slf4j
@Aspect
public class UseCaseRouterAroundAdvice {

    @Autowired
    private UseCaseRepository useCaseRepository;

    @Autowired
    private UseCaseEventPublisherManger useCaseEventPublisherManger;

    @Autowired
    private UseCaseExecutor useCaseExecutor;

    @Around("@within(cc.bucaedie.ucede.usecase.annotation.UseCaseServiceRouterExtension)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取拦截的class
        Class<?> useCaseServiceClass = proceedingJoinPoint.getTarget().getClass();

        UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(useCaseServiceClass, UseCaseService.class);
        // 获取方法
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        UseCase useCase = AnnotationUtils.findAnnotation(method,UseCase.class);
        if (useCase == null){// 判断方法是否有 UseCase 注解
            return proceedingJoinPoint.proceed();
        }
        String domain = useCaseServiceAnnotation.domain();
        String serviceCode = useCaseServiceAnnotation.serviceCode();
        String useCaseCode = useCase.code();
        // 获取入参
        Object[] args = proceedingJoinPoint.getArgs();
        // 获取业务用例信息
        UseCaseInfo useCaseInfo = useCaseRepository.getUseCase(domain,serviceCode,useCaseCode);
        // 获取业务用例执行的拦截器
        UseCaseExecuteInterceptor interceptor = useCaseInfo.getInterceptor();
        // 执行
        Object result = null;
        try {
            // 路由到实际的业务用例扩展执行
            result = useCaseExecutor.execute(domain, serviceCode, useCaseCode, args);
        }catch (Exception exception) {
            log.error("业务用例路由执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 发生异常,",exception);
            result = interceptor.exception(useCaseInfo,args,exception);
            // 转换结果为result // 只要在异常的时候记录就可以了， 其他情况有实际执行的业务用例记录相关情况，此处的异常主要针对找不到业务用例之类的
            UseCaseEvent useCaseEvent = interceptor.convertorResult2Event(useCaseInfo,args,result);
            if (useCaseEvent!=null){
                useCaseEvent.setDomain(useCaseInfo.getDomain());
                useCaseEvent.setUseCaseServiceCode(useCaseInfo.getServiceCode());
                useCaseEvent.setUseCaseCode(useCaseInfo.getCode());
                useCaseEvent.setUseCaseDesc(useCaseInfo.getDesc());
                useCaseEvent.setEventTime(new Date());
                if (StringUtils.isEmpty(useCaseEvent.getUuid())){// 补充事件的UUID
                    useCaseEvent.setUuid(UUIDUtils.getUUID());
                }
                // 补充触发当前用例的事件信息
                UseCaseEvent triggerEvent = UseCaseEventSubscriberDispatchContextHolder.get();
                if (triggerEvent!=null){
                    useCaseEvent.setTriggerUuid(triggerEvent.getUuid());
                }else{
                    useCaseEvent.setTriggerUuid("-1");// 默认值设置为 -1
                }
                // 推送相关事件
                useCaseEventPublisherManger.publishEvent(useCaseEvent);
            }else{
                log.error("业务用例路由执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 发生异常,转换业务事件为空");
            }
        }
        return result;
    }
}
