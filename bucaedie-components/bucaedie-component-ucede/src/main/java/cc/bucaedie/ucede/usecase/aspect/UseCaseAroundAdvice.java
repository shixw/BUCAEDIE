package cc.bucaedie.ucede.usecase.aspect;

import cc.bucaedie.ucede.commons.IdentityConstants;
import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.publiser.UseCaseEventPublisherManger;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventSubscriberDispatchContextHolder;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;
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

@Slf4j
@Aspect
public class UseCaseAroundAdvice {

    @Autowired
    private UseCaseRepository useCaseRepository;

    @Autowired
    private UseCaseEventPublisherManger useCaseEventPublisherManger;

    @Around("@within(cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> useCaseServiceClass = proceedingJoinPoint.getTarget().getClass();
        // 获取类注解
        UseCaseServiceExtension useCaseServiceExtensionAnnotation = AnnotationUtils.findAnnotation(useCaseServiceClass, UseCaseServiceExtension.class);
        UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(useCaseServiceClass, UseCaseService.class);
        // 获取方法
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        UseCase useCase = AnnotationUtils.findAnnotation(method,UseCase.class);
        if (useCase == null){// 判断方法是否有 UseCase 注解
            return proceedingJoinPoint.proceed();
        }
        // 用例开始执行时间
        Date useCaseExecuteTime = new Date();
        // 获取入参
        Object[] args = proceedingJoinPoint.getArgs();
        // 获取业务用例信息
        UseCaseInfo useCaseInfo = useCaseRepository.getUseCase(useCaseServiceAnnotation.domain(),useCaseServiceAnnotation.serviceCode(),useCase.code());
        // 获取业务用例执行的拦截器
        UseCaseExecuteInterceptor interceptor = useCaseInfo.getInterceptor();
        Object result = null;
        try {
            interceptor.before(useCaseInfo,args);
            result = proceedingJoinPoint.proceed();
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 参数校验失败,",illegalArgumentException);
            result = interceptor.exception(useCaseInfo,args,illegalArgumentException);
        }catch (Exception exception) {
            log.error("执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 发生异常,",exception);
            result = interceptor.exception(useCaseInfo,args,exception);
        }finally {
            // 此处可能会对result进行重新赋值....
            result = interceptor.complete(useCaseInfo,args,result);
            //用例执行结束时间
            Date useCaseExecuteEndTime = new Date();
            // 转换结果为result
            UseCaseEvent useCaseEvent = interceptor.convertorResult2Event(useCaseInfo,args,result);
            if (useCaseEvent!=null){
                useCaseEvent.setDomain(useCaseInfo.getDomain());
                useCaseEvent.setUseCaseServiceCode(useCaseInfo.getServiceCode());
                useCaseEvent.setUseCaseCode(useCaseInfo.getCode());
                useCaseEvent.setUseCaseDesc(useCaseInfo.getDesc());
                useCaseEvent.setUseCaseExecuteTime(useCaseExecuteTime);
                useCaseEvent.setEventTime(useCaseExecuteEndTime);
                useCaseEvent.setUseCaseExecuteDuration(useCaseExecuteEndTime.getTime()-useCaseExecuteTime.getTime());
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
                log.error("执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 最终转换业务事件为空");
            }
        }
        return result;
    }
}
