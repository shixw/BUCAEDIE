package cc.bucaedie.ucede.usecase.aspect;

import cc.bucaedie.ucede.commons.UUIDUtils;
import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.publiser.UseCaseEventPublisherManger;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseInfo;
import cc.bucaedie.ucede.usecase.UseCaseRepository;
import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
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

    @Around("@annotation(cc.bucaedie.ucede.usecase.annotation.UseCase) && execution(public * *(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取类注解
        UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(proceedingJoinPoint.getTarget().getClass(), UseCaseService.class);
        // 获取方法
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        UseCase useCase = AnnotationUtils.getAnnotation(method,UseCase.class);
        if (useCase == null){// 判断方法是否有 UseCase 注解
            return proceedingJoinPoint.proceed();
        }
        // 获取入参
        Object[] args = proceedingJoinPoint.getArgs();
        // 获取业务用例信息
        UseCaseInfo useCaseInfo = useCaseRepository.getUseCase(useCaseServiceAnnotation.domain(),useCase.code());
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
            // 转换结果为result
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
                // 推送相关事件
                useCaseEventPublisherManger.publishEvent(useCaseEvent);
            }else{
                log.error("执行业务领域:"+useCaseInfo.getDomain()+",服务:"+useCaseInfo.getServiceCode()+",用例:"+useCase.code()+" 最终转换业务事件为空");
            }
            // 此处可能会对result进行重新赋值....
            result = interceptor.complete(useCaseInfo,args,result);
        }
        return result;
    }
}
