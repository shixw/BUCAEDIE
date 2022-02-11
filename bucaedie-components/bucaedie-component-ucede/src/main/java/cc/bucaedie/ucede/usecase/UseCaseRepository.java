package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.usecase.annotation.UseCase;
import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceExtension;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceRouterExtension;
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
    public UseCaseInfo getUseCase(String domain,String serviceCode, String useCaseCode) {
        return useCaseRepo.get(UseCaseInfo.getUniqueKey(domain, serviceCode, useCaseCode));
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
     * 初始化:从spring上下文中加载相关的信息缓存起来
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //注册业务用例路由扩展
        Map<String, Object> useCaseRouterExtensionBeanServiceMap = applicationContext.getBeansWithAnnotation(UseCaseServiceRouterExtension.class);
        useCaseRouterExtensionBeanServiceMap.forEach((beanName,useCaseRouterExtensionBeanService)->{
            Class<?> userCaseServiceClass = useCaseRouterExtensionBeanService.getClass();
            // 校验互斥,路由扩展和实际扩展不能同时注解
            if (AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseServiceExtension.class)!=null){
                throw new IllegalArgumentException("业务用例路由："+beanName+" 不能同时包含注解 UseCaseServiceRouterExtension 和 UseCaseServiceExtension");
            }
            UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseService.class);
            Method[] declaredMethods = userCaseServiceClass.getDeclaredMethods();
            if (declaredMethods != null && declaredMethods.length > 0) {
                for (Method declaredMethod : declaredMethods) {
                    // 使用 AnnotationUtils 获取注解， 由于使用了 aop 所有会造成获取不到注解
                    UseCase useCaseAnnotation = AnnotationUtils.findAnnotation(declaredMethod, UseCase.class);
                    if (useCaseAnnotation != null) {
                        // 注册业务用例
                        UseCaseInfo useCaseInfo = this.registerUseCase(useCaseServiceAnnotation,useCaseAnnotation);
                        useCaseInfo.setRouterServiceBeanRef(useCaseRouterExtensionBeanService);
                        useCaseInfo.setRouterUseCaseMethod(declaredMethod);
                    }
                }
            }
        });
        //注册业务身份扩展
        Map<String, Object> useCaseBeanServiceMap = applicationContext.getBeansWithAnnotation(UseCaseServiceExtension.class);
        useCaseBeanServiceMap.forEach((beanName, userCaseService) -> {
            Class<?> userCaseServiceClass = userCaseService.getClass();
            UseCaseService useCaseServiceAnnotation = AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseService.class);
            UseCaseServiceExtension useCaseServiceExtensionAnnotation = AnnotationUtils.findAnnotation(userCaseServiceClass, UseCaseServiceExtension.class);
            String[] identities = useCaseServiceExtensionAnnotation.identities();// 业务此扩展试用的业务身份集合
            String domain = useCaseServiceAnnotation.domain();
            Method[] declaredMethods = userCaseServiceClass.getDeclaredMethods();
            if (declaredMethods != null && declaredMethods.length > 0) {
                for (Method declaredMethod : declaredMethods) {
                    // 使用 AnnotationUtils 获取注解， 由于使用了 aop 所有会造成获取不到注解
                    UseCase useCaseAnnotation = AnnotationUtils.findAnnotation(declaredMethod, UseCase.class);

                    if (useCaseAnnotation != null) {
                        // 注册业务用例
                        UseCaseInfo useCaseInfo = this.registerUseCase(useCaseServiceAnnotation,useCaseAnnotation);
                        // 注册业务用例扩展 // 由于试用的业务身份可能有多个所以需要遍历注册
                        for (String identity : identities) {
                            UseCaseExtensionInfo extensionInfo = new UseCaseExtensionInfo();
                            extensionInfo.setIdentity(identity);
                            extensionInfo.setServiceBeanRef(userCaseService);
                            extensionInfo.setUseCaseMethod(declaredMethod);
                            // 添加扩展实现
                            useCaseInfo.addExtensionInfo(extensionInfo);
                        }
                    }
                }
            }
        });
    }

    /**
     * 注册业务用例
     * @param useCaseServiceAnnotation
     * @param useCaseAnnotation
     * @return
     */
    private UseCaseInfo registerUseCase(UseCaseService useCaseServiceAnnotation, UseCase useCaseAnnotation) {
        // 校验业务用例是否已经存在
        String uniqueKey = UseCaseInfo.getUniqueKey(useCaseServiceAnnotation.domain(), useCaseServiceAnnotation.serviceCode(), useCaseAnnotation.code());
        if(!this.useCaseRepo.containsKey(uniqueKey)){
            UseCaseInfo useCase = new UseCaseInfo();
            useCase.setDomain(useCaseServiceAnnotation.domain());
            useCase.setServiceCode(useCaseServiceAnnotation.serviceCode());
            useCase.setCode(useCaseAnnotation.code());
            useCase.setDesc(useCaseAnnotation.desc());
            useCase.setEvents(useCaseAnnotation.events());
            Class<? extends UseCaseExecuteInterceptor> interceptor;
            if (useCaseAnnotation.interceptor() != DefaultUseCaseExecuteInterceptor.class) {
                interceptor = useCaseAnnotation.interceptor();
            } else if (useCaseServiceAnnotation.interceptor() != DefaultUseCaseExecuteInterceptor.class) {
                interceptor = useCaseServiceAnnotation.interceptor();
            } else {
                interceptor = DefaultUseCaseExecuteInterceptor.class;
                log.warn("领域:"+useCaseServiceAnnotation.domain()+",业务用例服务：" + useCaseServiceAnnotation.serviceCode() + ",业务用例：" + useCaseAnnotation.code() + " 未自定义业务用例执行拦截器,使用默认的拦截器!");
            }
            useCase.setInterceptor(getUseCaseInterceptorBean(interceptor));
            this.addUseCase(useCase);
        }
        return this.useCaseRepo.get(uniqueKey);
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
