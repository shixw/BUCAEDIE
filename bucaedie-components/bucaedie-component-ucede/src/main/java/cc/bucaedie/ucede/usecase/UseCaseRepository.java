package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务用例相关信息存储的仓库
 */
@Slf4j
public class UseCaseRepository implements InitializingBean, ApplicationContextAware {

    /**
     * 用来存储业务用例
     * key = 业务用例所属领域+业务用例的code
     */
    private Map<String, UseCaseInfo> useCaseRepo = new ConcurrentHashMap<>();

    /**
     * 获取业务用例信息
     */
    public UseCaseInfo getUseCase(String domain, String useCaseCode) {
        return useCaseRepo.get(getUseCaseUniqueKey(domain, useCaseCode));
    }

    /**
     * 获取所有的业务用例
     */
    public Map<String, UseCaseInfo> getAllUseCase() {
        return useCaseRepo;
    }

    /**
     * 添加业务用例信息
     */
    public void addUseCase(UseCaseInfo useCaseInfo) {
        useCaseRepo.put(useCaseInfo.getUniqueKey(), useCaseInfo);
    }

    /**
     * 获取业务用例的唯一标识
     *
     * @return
     */
    private String getUseCaseUniqueKey(String domain, String useCaseCode) {
        return domain + UseCaseInfo.UNIQUE_KEY_SPLIT_FLAG + useCaseCode;
    }

    /**
     * 初始化:从spring上下文中加载相关的信息缓存起来
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> useCaseBeanServiceMap = applicationContext.getBeansWithAnnotation(UseCaseService.class);
        useCaseBeanServiceMap.forEach((beanName, userCaseService) -> {
            Class<?> userCaseServiceClass = userCaseService.getClass();
            UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseService.class);
            String domain = useCaseServiceAnnotation.domain();
            Method[] declaredMethods = userCaseServiceClass.getDeclaredMethods();
            if (declaredMethods != null && declaredMethods.length > 0) {
                for (Method declaredMethod : declaredMethods) {
                    // 使用 AnnotationUtils 获取注解， 由于使用了 aop 所有会造成获取不到注解
                    UseCase useCaseAnnotation = AnnotationUtils.findAnnotation(declaredMethod, UseCase.class);

                    if (useCaseAnnotation != null) {
                        // // 注册用例信息
                        UseCaseInfo useCase = new UseCaseInfo();
                        useCase.setDomain(domain);
                        useCase.setServiceCode(beanName);
                        useCase.setCode(useCaseAnnotation.code());
                        useCase.setDesc(useCaseAnnotation.desc());
                        useCase.setServiceBeanRef(userCaseService);
                        useCase.setUseCaseMethod(declaredMethod);
                        useCase.setEvents(useCaseAnnotation.events());
                        Class<? extends UseCaseExecuteInterceptor> interceptor;
                        if (useCaseAnnotation.interceptor() != DefaultUseCaseExecuteInterceptor.class) {
                            interceptor = useCaseAnnotation.interceptor();
                        } else if (useCaseServiceAnnotation.interceptor() != DefaultUseCaseExecuteInterceptor.class) {
                            interceptor = useCaseServiceAnnotation.interceptor();
                        } else {
                            interceptor = DefaultUseCaseExecuteInterceptor.class;
                            log.warn("业务用例服务：" + beanName + ",业务用例：" + useCaseAnnotation.code() + " 未自定义业务用例执行拦截器,使用默认的拦截器!");
                        }
                        useCase.setInterceptor(getUseCaseInterceptorBean(interceptor));
                        this.addUseCase(useCase);
                    }
                }
            }
        });
    }


    /**
     * 获取拦截器Bean
     *
     * @return
     */
    private UseCaseExecuteInterceptor getUseCaseInterceptorBean(Class<? extends UseCaseExecuteInterceptor> interceptorClass) {
        UseCaseExecuteInterceptor interceptor = applicationContext.getBean(interceptorClass);
        if (interceptor == null) {
            throw new IllegalArgumentException("未查询到Class为:" + interceptorClass + " 对应的业务用例拦截器Bean实例");
        }
        return interceptor;
    }

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
